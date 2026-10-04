package com.gymmanagement.service;

import com.gymmanagement.dto.request.MembershipRequest;
import com.gymmanagement.dto.request.RenewRequest;
import com.gymmanagement.dto.response.MembershipResponse;
import com.gymmanagement.dto.response.VerificationResponse;
import com.gymmanagement.entity.Member;
import com.gymmanagement.entity.Membership;
import com.gymmanagement.entity.MembershipPlan;
import com.gymmanagement.entity.Payment;
import com.gymmanagement.enums.*;
import com.gymmanagement.exception.BadRequestException;
import com.gymmanagement.exception.ResourceNotFoundException;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipRepository;
import com.gymmanagement.repository.PaymentRepository;
import com.gymmanagement.repository.PlanRepository;
import com.gymmanagement.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberService memberService;

    public List<MembershipResponse> getAllMemberships() {
        return membershipRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MembershipResponse> getMembershipsByMemberId(String memberId) {
        Member member = memberService.getMemberEntityByMemberId(memberId);
        return membershipRepository.findByMemberIdOrderByStartDateDesc(member.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MembershipResponse getMembershipById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + id));
        return mapToResponse(membership);
    }

    @Transactional
    public MembershipResponse createMembership(MembershipRequest request) {
        Member member = memberService.getMemberEntityByMemberId(request.getMemberId());

        MembershipPlan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + request.getPlanId()));

        if (plan.getStatus() == PlanStatus.INACTIVE) {
            throw new BadRequestException("Cannot purchase an inactive plan");
        }

        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();

        // Check if member already has an active membership and adjust start date if needed
        Optional<Membership> activeOpt = membershipRepository.findActiveMembershipForMember(member.getId(), LocalDate.now());
        if (activeOpt.isPresent()) {
            Membership active = activeOpt.get();
            if (active.getEndDate().isAfter(startDate) || active.getEndDate().isEqual(startDate)) {
                startDate = active.getEndDate().plusDays(1);
            }
        }

        LocalDate endDate = DateUtils.calculateEndDate(startDate, plan.getDurationValue(), plan.getDurationUnit());
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("Membership end date cannot be before start date");
        }

        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        BigDecimal finalAmount = plan.getPrice().subtract(discount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        Membership membership = Membership.builder()
                .member(member)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .status(MembershipStatus.ACTIVE)
                .amount(plan.getPrice())
                .discount(discount)
                .finalAmount(finalAmount)
                .build();

        Membership savedMembership = membershipRepository.save(membership);

        // Record associated payment
        Payment payment = Payment.builder()
                .member(member)
                .membership(savedMembership)
                .amount(plan.getPrice())
                .discount(discount)
                .finalAmount(finalAmount)
                .paymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : PaymentMethod.CASH)
                .paymentStatus(PaymentStatus.PAID)
                .transactionReference(request.getTransactionReference())
                .paymentDate(LocalDateTime.now())
                .notes(request.getNotes() != null ? request.getNotes() : "New Membership Purchase: " + plan.getName())
                .build();

        paymentRepository.save(payment);

        // Update member status to ACTIVE
        member.setStatus(MemberStatus.ACTIVE);
        memberRepository.save(member);

        return mapToResponse(savedMembership);
    }

    @Transactional
    public MembershipResponse renewMembership(Long membershipId, RenewRequest request) {
        Membership existingMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + membershipId));

        Member member = existingMembership.getMember();
        MembershipPlan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + request.getPlanId()));

        if (plan.getStatus() == PlanStatus.INACTIVE) {
            throw new BadRequestException("Cannot renew using an inactive plan");
        }

        LocalDate today = LocalDate.now();
        LocalDate newStartDate;

        // RENEWAL BUSINESS RULE:
        // If active (today <= existingEndDate): start after existing end date
        // If expired (today > existingEndDate): start today
        if (existingMembership.isCurrentlyActive() && !existingMembership.getEndDate().isBefore(today)) {
            newStartDate = existingMembership.getEndDate().plusDays(1);
        } else {
            newStartDate = today;
        }

        LocalDate newEndDate = DateUtils.calculateEndDate(newStartDate, plan.getDurationValue(), plan.getDurationUnit());

        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        BigDecimal finalAmount = plan.getPrice().subtract(discount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        Membership newMembership = Membership.builder()
                .member(member)
                .plan(plan)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .status(MembershipStatus.ACTIVE)
                .amount(plan.getPrice())
                .discount(discount)
                .finalAmount(finalAmount)
                .build();

        Membership saved = membershipRepository.save(newMembership);

        // Record Payment for Renewal
        Payment payment = Payment.builder()
                .member(member)
                .membership(saved)
                .amount(plan.getPrice())
                .discount(discount)
                .finalAmount(finalAmount)
                .paymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : PaymentMethod.CASH)
                .paymentStatus(PaymentStatus.PAID)
                .transactionReference(request.getTransactionReference())
                .paymentDate(LocalDateTime.now())
                .notes(request.getNotes() != null ? request.getNotes() : "Membership Renewal: " + plan.getName())
                .build();

        paymentRepository.save(payment);

        member.setStatus(MemberStatus.ACTIVE);
        memberRepository.save(member);

        return mapToResponse(saved);
    }

    public VerificationResponse verifyMembership(String memberId) {
        Member member;
        try {
            member = memberService.getMemberEntityByMemberId(memberId);
        } catch (ResourceNotFoundException e) {
            return VerificationResponse.builder()
                    .memberId(memberId)
                    .verificationStatus("NOT_FOUND")
                    .message("Member ID not found in system")
                    .allowAccess(false)
                    .build();
        }

        if (member.getStatus() == MemberStatus.SUSPENDED) {
            return VerificationResponse.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getFullName())
                    .phone(member.getPhone())
                    .email(member.getEmail())
                    .profilePhoto(member.getProfilePhoto())
                    .verificationStatus("SUSPENDED")
                    .message("MEMBER IS SUSPENDED. ACCESS DENIED.")
                    .allowAccess(false)
                    .build();
        }

        LocalDate today = LocalDate.now();
        List<Membership> memberships = membershipRepository.findByMemberIdOrderByStartDateDesc(member.getId());

        if (memberships.isEmpty()) {
            return VerificationResponse.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getFullName())
                    .phone(member.getPhone())
                    .email(member.getEmail())
                    .profilePhoto(member.getProfilePhoto())
                    .verificationStatus("NO_MEMBERSHIP")
                    .message("No membership records found for member")
                    .allowAccess(false)
                    .build();
        }

        // Find current or latest membership
        Membership latest = memberships.get(0);
        long daysRemaining = DateUtils.calculateDaysRemaining(latest.getEndDate());

        boolean isCurrentlyValid = !today.isBefore(latest.getStartDate()) && !today.isAfter(latest.getEndDate()) && latest.getStatus() == MembershipStatus.ACTIVE;

        if (isCurrentlyValid) {
            if (daysRemaining <= 7) {
                return VerificationResponse.builder()
                        .memberId(member.getMemberId())
                        .memberName(member.getFullName())
                        .phone(member.getPhone())
                        .email(member.getEmail())
                        .profilePhoto(member.getProfilePhoto())
                        .planName(latest.getPlan().getName())
                        .startDate(latest.getStartDate())
                        .endDate(latest.getEndDate())
                        .verificationStatus("EXPIRING_SOON")
                        .daysRemaining(daysRemaining)
                        .message("MEMBERSHIP EXPIRING SOON (" + daysRemaining + " days remaining)")
                        .allowAccess(true)
                        .build();
            } else {
                return VerificationResponse.builder()
                        .memberId(member.getMemberId())
                        .memberName(member.getFullName())
                        .phone(member.getPhone())
                        .email(member.getEmail())
                        .profilePhoto(member.getProfilePhoto())
                        .planName(latest.getPlan().getName())
                        .startDate(latest.getStartDate())
                        .endDate(latest.getEndDate())
                        .verificationStatus("ACTIVE")
                        .daysRemaining(daysRemaining)
                        .message("MEMBERSHIP ACTIVE")
                        .allowAccess(true)
                        .build();
            }
        } else {
            return VerificationResponse.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getFullName())
                    .phone(member.getPhone())
                    .email(member.getEmail())
                    .profilePhoto(member.getProfilePhoto())
                    .planName(latest.getPlan().getName())
                    .startDate(latest.getStartDate())
                    .endDate(latest.getEndDate())
                    .verificationStatus("EXPIRED")
                    .daysRemaining(0)
                    .message("MEMBERSHIP EXPIRED on " + latest.getEndDate() + ". ACCESS DENIED.")
                    .allowAccess(false)
                    .build();
        }
    }

    public MembershipResponse mapToResponse(Membership membership) {
        LocalDate today = LocalDate.now();
        boolean expired = today.isAfter(membership.getEndDate()) || membership.getStatus() == MembershipStatus.EXPIRED;
        long daysRemaining = DateUtils.calculateDaysRemaining(membership.getEndDate());
        boolean expiringSoon = !expired && daysRemaining <= 7;

        MembershipStatus dynamicStatus = membership.getStatus();
        if (expired && dynamicStatus != MembershipStatus.CANCELLED && dynamicStatus != MembershipStatus.SUSPENDED) {
            dynamicStatus = MembershipStatus.EXPIRED;
        }

        return MembershipResponse.builder()
                .id(membership.getId())
                .memberDbId(membership.getMember().getId())
                .memberId(membership.getMember().getMemberId())
                .memberName(membership.getMember().getFullName())
                .planId(membership.getPlan().getId())
                .planName(membership.getPlan().getName())
                .startDate(membership.getStartDate())
                .endDate(membership.getEndDate())
                .status(dynamicStatus)
                .amount(membership.getAmount())
                .discount(membership.getDiscount())
                .finalAmount(membership.getFinalAmount())
                .daysRemaining(daysRemaining)
                .isExpired(expired)
                .isExpiringSoon(expiringSoon)
                .createdAt(membership.getCreatedAt())
                .updatedAt(membership.getUpdatedAt())
                .build();
    }
}
