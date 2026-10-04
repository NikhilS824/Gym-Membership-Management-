package com.gymmanagement.service;

import com.gymmanagement.dto.request.MemberRequest;
import com.gymmanagement.dto.response.MemberResponse;
import com.gymmanagement.entity.Member;
import com.gymmanagement.entity.Trainer;
import com.gymmanagement.enums.MemberStatus;
import com.gymmanagement.exception.BadRequestException;
import com.gymmanagement.exception.ResourceNotFoundException;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.TrainerRepository;
import com.gymmanagement.util.MemberIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private MemberIdGenerator memberIdGenerator;

    public Page<MemberResponse> searchMembers(String query, MemberStatus status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Member> membersPage = memberRepository.searchMembers(query, status, pageable);

        return membersPage.map(this::mapToResponse);
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return mapToResponse(member);
    }

    public MemberResponse getMemberByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with Member ID: " + memberId));
        return mapToResponse(member);
    }

    public Member getMemberEntityByMemberId(String memberId) {
        // Accepts both formatted (GM00001) or numeric (1)
        if (memberId.toUpperCase().startsWith("GM")) {
            return memberRepository.findByMemberId(memberId.toUpperCase())
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));
        } else {
            try {
                Long id = Long.parseLong(memberId);
                return memberRepository.findById(id)
                        .orElseGet(() -> memberRepository.findByMemberId(String.format("GM%05d", id))
                                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId)));
            } catch (NumberFormatException e) {
                return memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));
            }
        }
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (memberRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email is already registered: " + request.getEmail());
            }
        }

        String autoMemberId = memberIdGenerator.generateNextMemberId();

        Trainer trainer = null;
        if (request.getTrainerId() != null) {
            trainer = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + request.getTrainerId()));
        }

        Member member = Member.builder()
                .memberId(autoMemberId)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .age(request.getAge())
                .gender(request.getGender())
                .address(request.getAddress())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .joiningDate(request.getJoiningDate())
                .profilePhoto(request.getProfilePhoto())
                .status(request.getStatus() != null ? request.getStatus() : MemberStatus.ACTIVE)
                .trainer(trainer)
                .build();

        Member saved = memberRepository.save(member);
        return mapToResponse(saved);
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (!request.getEmail().equalsIgnoreCase(member.getEmail()) && memberRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email is already in use: " + request.getEmail());
            }
        }

        Trainer trainer = member.getTrainer();
        if (request.getTrainerId() != null) {
            trainer = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + request.getTrainerId()));
        } else if (request.getTrainerId() == null && member.getTrainer() != null) {
            trainer = null;
        }

        member.setFullName(request.getFullName());
        member.setPhone(request.getPhone());
        member.setEmail(request.getEmail());
        member.setDateOfBirth(request.getDateOfBirth());
        member.setAge(request.getAge());
        member.setGender(request.getGender());
        member.setAddress(request.getAddress());
        member.setEmergencyContactName(request.getEmergencyContactName());
        member.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getJoiningDate() != null) {
            member.setJoiningDate(request.getJoiningDate());
        }
        if (request.getProfilePhoto() != null) {
            member.setProfilePhoto(request.getProfilePhoto());
        }
        if (request.getStatus() != null) {
            member.setStatus(request.getStatus());
        }
        member.setTrainer(trainer);

        Member updated = memberRepository.save(member);
        return mapToResponse(updated);
    }

    @Transactional
    public MemberResponse updateMemberStatus(Long id, MemberStatus status) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        member.setStatus(status);
        Member updated = memberRepository.save(member);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        // Soft delete preference: set status to INACTIVE
        member.setStatus(MemberStatus.INACTIVE);
        memberRepository.save(member);
    }

    public MemberResponse mapToResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .fullName(member.getFullName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .dateOfBirth(member.getDateOfBirth())
                .age(member.getAge())
                .gender(member.getGender())
                .address(member.getAddress())
                .emergencyContactName(member.getEmergencyContactName())
                .emergencyContactPhone(member.getEmergencyContactPhone())
                .joiningDate(member.getJoiningDate())
                .profilePhoto(member.getProfilePhoto())
                .status(member.getStatus())
                .trainerId(member.getTrainer() != null ? member.getTrainer().getId() : null)
                .trainerName(member.getTrainer() != null ? member.getTrainer().getName() : null)
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
