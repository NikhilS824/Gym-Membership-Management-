package com.gymmanagement.controller;

import com.gymmanagement.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/member")
    public ResponseEntity<Map<String, Object>> getMemberReports() {
        return ResponseEntity.ok(reportService.getMemberReports());
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueReports() {
        return ResponseEntity.ok(reportService.getRevenueReports());
    }

    @GetMapping("/membership")
    public ResponseEntity<Map<String, Object>> getMembershipReports() {
        return ResponseEntity.ok(reportService.getMembershipReports());
    }

    @GetMapping("/attendance")
    public ResponseEntity<Map<String, Object>> getAttendanceReports() {
        return ResponseEntity.ok(reportService.getAttendanceReports());
    }
}
