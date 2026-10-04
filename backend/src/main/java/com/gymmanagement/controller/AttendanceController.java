package com.gymmanagement.controller;

import com.gymmanagement.dto.request.CheckInRequest;
import com.gymmanagement.dto.response.AttendanceResponse;
import com.gymmanagement.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAttendance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date != null) {
            return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
        }
        return ResponseEntity.ok(attendanceService.getTodayAttendance());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByMemberId(@PathVariable String memberId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByMemberId(memberId));
    }

    @PostMapping("/check-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        AttendanceResponse response = attendanceService.checkIn(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/check-out/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<AttendanceResponse> checkOut(@PathVariable Long id) {
        AttendanceResponse response = attendanceService.checkOut(id);
        return ResponseEntity.ok(response);
    }
}
