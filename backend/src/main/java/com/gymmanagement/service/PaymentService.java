package com.gymmanagement.service;

import com.gymmanagement.dto.request.PaymentRequest;
import com.gymmanagement.dto.response.PaymentResponse;
import com.gymmanagement.entity.Member;
import com.gymmanagement.entity.Membership;
import com.gymmanagement.entity.Payment;
import com.gymmanagement.enums.PaymentStatus;
import com.gymmanagement.exception.ResourceNotFoundException;
import com.gymmanagement.repository.MembershipRepository;
import com.gymmanagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MemberService memberService;

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByMemberId(String memberId) {
        return paymentRepository.findByMemberMemberIdOrderByPaymentDateDesc(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return mapToResponse(payment);
    }

    @Transactional
    public PaymentResponse recordPayment(PaymentRequest request) {
        Member member = memberService.getMemberEntityByMemberId(request.getMemberId());

        Membership membership = null;
        if (request.getMembershipId() != null) {
            membership = membershipRepository.findById(request.getMembershipId()).orElse(null);
        }

        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        BigDecimal finalAmount = request.getAmount().subtract(discount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        Payment payment = Payment.builder()
                .member(member)
                .membership(membership)
                .amount(request.getAmount())
                .discount(discount)
                .finalAmount(finalAmount)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : PaymentStatus.PAID)
                .transactionReference(request.getTransactionReference())
                .paymentDate(LocalDateTime.now())
                .notes(request.getNotes())
                .build();

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    public PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .memberDbId(payment.getMember().getId())
                .memberId(payment.getMember().getMemberId())
                .memberName(payment.getMember().getFullName())
                .memberPhone(payment.getMember().getPhone())
                .memberEmail(payment.getMember().getEmail())
                .membershipId(payment.getMembership() != null ? payment.getMembership().getId() : null)
                .planName(payment.getMembership() != null ? payment.getMembership().getPlan().getName() : "General Fee")
                .amount(payment.getAmount())
                .discount(payment.getDiscount())
                .finalAmount(payment.getFinalAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionReference(payment.getTransactionReference())
                .paymentDate(payment.getPaymentDate())
                .notes(payment.getNotes())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
