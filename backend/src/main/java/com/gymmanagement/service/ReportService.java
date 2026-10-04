package com.gymmanagement.service;

import com.gymmanagement.enums.MemberStatus;

import com.gymmanagement.repository.AttendanceRepository;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipRepository;
import com.gymmanagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Map<String, Object> getMemberReports() {
        Map<String, Object> report = new HashMap<>();
        report.put("totalMembers", memberRepository.count());
        report.put("activeMembers", memberRepository.countByStatus(MemberStatus.ACTIVE));
        report.put("inactiveMembers", memberRepository.countByStatus(MemberStatus.INACTIVE));
        report.put("suspendedMembers", memberRepository.countByStatus(MemberStatus.SUSPENDED));
        report.put("newMembers30Days", memberRepository.countNewMembersLast30Days());
        return report;
    }

    public Map<String, Object> getRevenueReports() {
        Map<String, Object> report = new HashMap<>();
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        BigDecimal dailyRevenue = paymentRepository.calculateRevenueBetween(startOfDay, endOfDay);
        if (dailyRevenue == null) dailyRevenue = BigDecimal.ZERO;

        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);
        BigDecimal monthlyRevenue = paymentRepository.calculateRevenueBetween(startOfMonth, endOfMonth);
        if (monthlyRevenue == null) monthlyRevenue = BigDecimal.ZERO;

        LocalDateTime startOfYear = today.withDayOfYear(1).atStartOfDay();
        LocalDateTime endOfYear = today.withDayOfYear(today.lengthOfYear()).atTime(23, 59, 59);
        BigDecimal yearlyRevenue = paymentRepository.calculateRevenueBetween(startOfYear, endOfYear);
        if (yearlyRevenue == null) yearlyRevenue = BigDecimal.ZERO;

        BigDecimal totalRevenue = paymentRepository.calculateTotalRevenue();
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        report.put("dailyRevenue", dailyRevenue);
        report.put("monthlyRevenue", monthlyRevenue);
        report.put("yearlyRevenue", yearlyRevenue);
        report.put("totalRevenue", totalRevenue);

        List<Object[]> monthlyData = paymentRepository.findMonthlyRevenueGrouped();
        List<Map<String, Object>> monthlyRevenueList = new ArrayList<>();
        for (Object[] row : monthlyData) {
            Integer year = (Integer) row[0];
            Integer month = (Integer) row[1];
            BigDecimal total = (BigDecimal) row[2];

            Map<String, Object> item = new HashMap<>();
            item.put("year", year);
            item.put("month", month);
            item.put("label", String.format("%d-%02d", year, month));
            item.put("revenue", total);
            monthlyRevenueList.add(item);
        }
        report.put("monthlyRevenueBreakdown", monthlyRevenueList);

        return report;
    }

    public Map<String, Object> getMembershipReports() {
        Map<String, Object> report = new HashMap<>();
        LocalDate today = LocalDate.now();

        report.put("activeMemberships", membershipRepository.countActiveMemberships(today));
        report.put("expiredMemberships", membershipRepository.countExpiredMemberships(today));
        report.put("expiringIn7Days", membershipRepository.countExpiringSoonMemberships(today, today.plusDays(7)));

        List<Object[]> popularPlans = membershipRepository.findPopularPlansCount();
        List<Map<String, Object>> planStats = new ArrayList<>();
        for (Object[] row : popularPlans) {
            planStats.add(Map.of("planName", row[0], "count", row[1]));
        }
        report.put("popularPlans", planStats);

        return report;
    }

    public Map<String, Object> getAttendanceReports() {
        Map<String, Object> report = new HashMap<>();
        LocalDate today = LocalDate.now();

        report.put("todayAttendance", attendanceRepository.countByDate(today));

        LocalDate thirtyDaysAgo = today.minusDays(29);
        List<Object[]> attTrend = attendanceRepository.findAttendanceTrendSince(thirtyDaysAgo);
        List<Map<String, Object>> trendList = new ArrayList<>();
        for (Object[] row : attTrend) {
            trendList.add(Map.of("date", row[0].toString(), "count", row[1]));
        }
        report.put("attendanceTrend30Days", trendList);

        return report;
    }
}
