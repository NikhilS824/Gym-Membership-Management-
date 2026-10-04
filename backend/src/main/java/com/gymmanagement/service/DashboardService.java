package com.gymmanagement.service;

import com.gymmanagement.dto.response.DashboardSummaryResponse;
import com.gymmanagement.dto.response.MembershipResponse;
import com.gymmanagement.enums.MemberStatus;
import com.gymmanagement.enums.MembershipStatus;
import com.gymmanagement.repository.AttendanceRepository;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipRepository;
import com.gymmanagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MembershipService membershipService;

    public DashboardSummaryResponse getDashboardSummary() {
        LocalDate today = LocalDate.now();

        long totalMembers = memberRepository.count();
        long activeMembers = memberRepository.countByStatus(MemberStatus.ACTIVE);
        long expiredMembers = membershipRepository.countExpiredMemberships(today);

        long expiringToday = membershipRepository.countExpiringSoonMemberships(today, today);
        long expiringIn3Days = membershipRepository.countExpiringSoonMemberships(today, today.plusDays(3));
        long expiringSoon = membershipRepository.countExpiringSoonMemberships(today, today.plusDays(7));
        long expiringIn30Days = membershipRepository.countExpiringSoonMemberships(today, today.plusDays(30));

        long todayAttendance = attendanceRepository.countByDate(today);
        long newMembers = memberRepository.countNewMembersLast30Days();

        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);
        BigDecimal monthlyRevenue = paymentRepository.calculateRevenueBetween(startOfMonth, endOfMonth);
        if (monthlyRevenue == null) monthlyRevenue = BigDecimal.ZERO;

        BigDecimal totalRevenue = paymentRepository.calculateTotalRevenue();
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        // Revenue Chart (Daily revenue for last 14 days)
        List<Map<String, Object>> revenueChart = new ArrayList<>();
        LocalDateTime fourteenDaysAgo = today.minusDays(13).atStartOfDay();
        List<Object[]> dailyRev = paymentRepository.findDailyRevenueSince(fourteenDaysAgo);
        Map<String, BigDecimal> revMap = new HashMap<>();
        for (Object[] row : dailyRev) {
            String dateStr = row[0].toString();
            BigDecimal amount = (BigDecimal) row[1];
            revMap.put(dateStr, amount);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        for (int i = 13; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String rawDateKey = date.toString();
            String displayLabel = date.format(formatter);
            BigDecimal val = revMap.getOrDefault(rawDateKey, BigDecimal.ZERO);

            Map<String, Object> point = new HashMap<>();
            point.put("date", displayLabel);
            point.put("revenue", val);
            revenueChart.add(point);
        }

        // Membership Status Distribution Chart
        List<Map<String, Object>> membershipStatusChart = new ArrayList<>();
        membershipStatusChart.add(Map.of("name", "Active", "value", activeMembers, "color", "#10B981"));
        membershipStatusChart.add(Map.of("name", "Expiring Soon", "value", expiringSoon, "color", "#F59E0B"));
        membershipStatusChart.add(Map.of("name", "Expired", "value", expiredMembers, "color", "#EF4444"));

        // Attendance Trend Chart (last 7 days)
        List<Map<String, Object>> attendanceChart = new ArrayList<>();
        LocalDate sevenDaysAgo = today.minusDays(6);
        List<Object[]> attTrend = attendanceRepository.findAttendanceTrendSince(sevenDaysAgo);
        Map<String, Long> attMap = new HashMap<>();
        for (Object[] row : attTrend) {
            String d = row[0].toString();
            Long cnt = (Long) row[1];
            attMap.put(d, cnt);
        }

        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            String rawKey = d.toString();
            String displayLabel = d.format(DateTimeFormatter.ofPattern("EEE (MMM dd)"));
            Long count = attMap.getOrDefault(rawKey, 0L);

            Map<String, Object> point = new HashMap<>();
            point.put("day", displayLabel);
            point.put("attendance", count);
            attendanceChart.add(point);
        }

        // Popular Plans Chart
        List<Map<String, Object>> popularPlansChart = new ArrayList<>();
        List<Object[]> popularPlans = membershipRepository.findPopularPlansCount();
        for (Object[] row : popularPlans) {
            String planName = (String) row[0];
            Long count = (Long) row[1];
            popularPlansChart.add(Map.of("plan", planName, "count", count));
        }

        // List of members expiring within 7 days
        List<MembershipResponse> expiringList = membershipRepository.findExpiringMemberships(today, today.plusDays(7))
                .stream()
                .map(membershipService::mapToResponse)
                .toList();

        return DashboardSummaryResponse.builder()
                .totalMembers(totalMembers)
                .activeMembers(activeMembers)
                .expiredMembers(expiredMembers)
                .expiringSoon(expiringSoon)
                .expiringToday(expiringToday)
                .expiringIn3Days(expiringIn3Days)
                .expiringIn30Days(expiringIn30Days)
                .todayAttendance(todayAttendance)
                .newMembers(newMembers)
                .monthlyRevenue(monthlyRevenue)
                .totalRevenue(totalRevenue)
                .revenueChart(revenueChart)
                .membershipStatusChart(membershipStatusChart)
                .attendanceChart(attendanceChart)
                .popularPlansChart(popularPlansChart)
                .expiringMemberships(expiringList)
                .build();
    }
}
