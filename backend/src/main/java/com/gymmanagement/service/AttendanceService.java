package com.gymmanagement.service;

import com.gymmanagement.dto.request.CheckInRequest;
import com.gymmanagement.dto.response.AttendanceResponse;
import com.gymmanagement.entity.Attendance;
import com.gymmanagement.entity.Member;
import com.gymmanagement.entity.Membership;
import com.gymmanagement.exception.BadRequestException;
import com.gymmanagement.exception.ResourceNotFoundException;
import com.gymmanagement.repository.AttendanceRepository;
import com.gymmanagement.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MemberService memberService;

    public List<AttendanceResponse> getTodayAttendance() {
        return attendanceRepository.findByDateOrderByCheckInTimeDesc(LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDateOrderByCheckInTimeDesc(date).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByMemberId(String memberId) {
        return attendanceRepository.findByMemberMemberIdOrderByCheckInTimeDesc(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AttendanceResponse checkIn(CheckInRequest request) {
        Member member = memberService.getMemberEntityByMemberId(request.getMemberId());

        LocalDate today = LocalDate.now();

        // 1. Verify Active Membership
        Optional<Membership> activeMembership = membershipRepository.findActiveMembershipForMember(member.getId(), today);
        if (activeMembership.isEmpty()) {
            throw new BadRequestException("MEMBERSHIP EXPIRED. ACCESS DENIED.");
        }

        // 2. Prevent duplicate open check-in on the same day
        Optional<Attendance> openAttendance = attendanceRepository.findOpenAttendanceForMemberOnDate(member.getId(), today);
        if (openAttendance.isPresent()) {
            throw new BadRequestException("Member " + member.getMemberId() + " (" + member.getFullName() + ") is already checked in today.");
        }

        Attendance attendance = Attendance.builder()
                .member(member)
                .checkInTime(LocalDateTime.now())
                .date(today)
                .status("PRESENT")
                .build();

        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponse(saved);
    }

    @Transactional
    public AttendanceResponse checkOut(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + attendanceId));

        if (attendance.getCheckOutTime() != null) {
            throw new BadRequestException("Member already checked out at " + attendance.getCheckOutTime());
        }

        attendance.setCheckOutTime(LocalDateTime.now());
        Attendance updated = attendanceRepository.save(attendance);
        return mapToResponse(updated);
    }

    public AttendanceResponse mapToResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .memberDbId(attendance.getMember().getId())
                .memberId(attendance.getMember().getMemberId())
                .memberName(attendance.getMember().getFullName())
                .memberPhone(attendance.getMember().getPhone())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .date(attendance.getDate())
                .status(attendance.getStatus())
                .createdAt(attendance.getCreatedAt())
                .build();
    }
}
