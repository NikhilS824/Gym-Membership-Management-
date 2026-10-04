package com.gymmanagement.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardSummaryResponse {
    private long totalMembers;
    private long activeMembers;
    private long expiredMembers;
    private long expiringSoon;
    private long expiringToday;
    private long expiringIn3Days;
    private long expiringIn30Days;
    private long todayAttendance;
    private long newMembers;
    private BigDecimal monthlyRevenue;
    private BigDecimal totalRevenue;

    private List<Map<String, Object>> revenueChart;
    private List<Map<String, Object>> membershipStatusChart;
    private List<Map<String, Object>> attendanceChart;
    private List<Map<String, Object>> popularPlansChart;

    private List<MembershipResponse> expiringMemberships;

    public DashboardSummaryResponse() {}

    public DashboardSummaryResponse(long totalMembers, long activeMembers, long expiredMembers, long expiringSoon, long expiringToday, long expiringIn3Days, long expiringIn30Days, long todayAttendance, long newMembers, BigDecimal monthlyRevenue, BigDecimal totalRevenue, List<Map<String, Object>> revenueChart, List<Map<String, Object>> membershipStatusChart, List<Map<String, Object>> attendanceChart, List<Map<String, Object>> popularPlansChart, List<MembershipResponse> expiringMemberships) {
        this.totalMembers = totalMembers;
        this.activeMembers = activeMembers;
        this.expiredMembers = expiredMembers;
        this.expiringSoon = expiringSoon;
        this.expiringToday = expiringToday;
        this.expiringIn3Days = expiringIn3Days;
        this.expiringIn30Days = expiringIn30Days;
        this.todayAttendance = todayAttendance;
        this.newMembers = newMembers;
        this.monthlyRevenue = monthlyRevenue;
        this.totalRevenue = totalRevenue;
        this.revenueChart = revenueChart;
        this.membershipStatusChart = membershipStatusChart;
        this.attendanceChart = attendanceChart;
        this.popularPlansChart = popularPlansChart;
        this.expiringMemberships = expiringMemberships;
    }

    public long getTotalMembers() { return totalMembers; }
    public void setTotalMembers(long totalMembers) { this.totalMembers = totalMembers; }

    public long getActiveMembers() { return activeMembers; }
    public void setActiveMembers(long activeMembers) { this.activeMembers = activeMembers; }

    public long getExpiredMembers() { return expiredMembers; }
    public void setExpiredMembers(long expiredMembers) { this.expiredMembers = expiredMembers; }

    public long getExpiringSoon() { return expiringSoon; }
    public void setExpiringSoon(long expiringSoon) { this.expiringSoon = expiringSoon; }

    public long getExpiringToday() { return expiringToday; }
    public void setExpiringToday(long expiringToday) { this.expiringToday = expiringToday; }

    public long getExpiringIn3Days() { return expiringIn3Days; }
    public void setExpiringIn3Days(long expiringIn3Days) { this.expiringIn3Days = expiringIn3Days; }

    public long getExpiringIn30Days() { return expiringIn30Days; }
    public void setExpiringIn30Days(long expiringIn30Days) { this.expiringIn30Days = expiringIn30Days; }

    public long getTodayAttendance() { return todayAttendance; }
    public void setTodayAttendance(long todayAttendance) { this.todayAttendance = todayAttendance; }

    public long getNewMembers() { return newMembers; }
    public void setNewMembers(long newMembers) { this.newMembers = newMembers; }

    public BigDecimal getMonthlyRevenue() { return monthlyRevenue; }
    public void setMonthlyRevenue(BigDecimal monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public List<Map<String, Object>> getRevenueChart() { return revenueChart; }
    public void setRevenueChart(List<Map<String, Object>> revenueChart) { this.revenueChart = revenueChart; }

    public List<Map<String, Object>> getMembershipStatusChart() { return membershipStatusChart; }
    public void setMembershipStatusChart(List<Map<String, Object>> membershipStatusChart) { this.membershipStatusChart = membershipStatusChart; }

    public List<Map<String, Object>> getAttendanceChart() { return attendanceChart; }
    public void setAttendanceChart(List<Map<String, Object>> attendanceChart) { this.attendanceChart = attendanceChart; }

    public List<Map<String, Object>> getPopularPlansChart() { return popularPlansChart; }
    public void setPopularPlansChart(List<Map<String, Object>> popularPlansChart) { this.popularPlansChart = popularPlansChart; }

    public List<MembershipResponse> getExpiringMemberships() { return expiringMemberships; }
    public void setExpiringMemberships(List<MembershipResponse> expiringMemberships) { this.expiringMemberships = expiringMemberships; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long totalMembers;
        private long activeMembers;
        private long expiredMembers;
        private long expiringSoon;
        private long expiringToday;
        private long expiringIn3Days;
        private long expiringIn30Days;
        private long todayAttendance;
        private long newMembers;
        private BigDecimal monthlyRevenue;
        private BigDecimal totalRevenue;
        private List<Map<String, Object>> revenueChart;
        private List<Map<String, Object>> membershipStatusChart;
        private List<Map<String, Object>> attendanceChart;
        private List<Map<String, Object>> popularPlansChart;
        private List<MembershipResponse> expiringMemberships;

        public Builder totalMembers(long totalMembers) { this.totalMembers = totalMembers; return this; }
        public Builder activeMembers(long activeMembers) { this.activeMembers = activeMembers; return this; }
        public Builder expiredMembers(long expiredMembers) { this.expiredMembers = expiredMembers; return this; }
        public Builder expiringSoon(long expiringSoon) { this.expiringSoon = expiringSoon; return this; }
        public Builder expiringToday(long expiringToday) { this.expiringToday = expiringToday; return this; }
        public Builder expiringIn3Days(long expiringIn3Days) { this.expiringIn3Days = expiringIn3Days; return this; }
        public Builder expiringIn30Days(long expiringIn30Days) { this.expiringIn30Days = expiringIn30Days; return this; }
        public Builder todayAttendance(long todayAttendance) { this.todayAttendance = todayAttendance; return this; }
        public Builder newMembers(long newMembers) { this.newMembers = newMembers; return this; }
        public Builder monthlyRevenue(BigDecimal monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; return this; }
        public Builder totalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public Builder revenueChart(List<Map<String, Object>> revenueChart) { this.revenueChart = revenueChart; return this; }
        public Builder membershipStatusChart(List<Map<String, Object>> membershipStatusChart) { this.membershipStatusChart = membershipStatusChart; return this; }
        public Builder attendanceChart(List<Map<String, Object>> attendanceChart) { this.attendanceChart = attendanceChart; return this; }
        public Builder popularPlansChart(List<Map<String, Object>> popularPlansChart) { this.popularPlansChart = popularPlansChart; return this; }
        public Builder expiringMemberships(List<MembershipResponse> expiringMemberships) { this.expiringMemberships = expiringMemberships; return this; }

        public DashboardSummaryResponse build() {
            return new DashboardSummaryResponse(totalMembers, activeMembers, expiredMembers, expiringSoon, expiringToday, expiringIn3Days, expiringIn30Days, todayAttendance, newMembers, monthlyRevenue, totalRevenue, revenueChart, membershipStatusChart, attendanceChart, popularPlansChart, expiringMemberships);
        }
    }
}
