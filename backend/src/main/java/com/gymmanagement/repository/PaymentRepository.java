package com.gymmanagement.repository;

import com.gymmanagement.entity.Payment;
import com.gymmanagement.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByMemberIdOrderByPaymentDateDesc(Long memberId);

    @Query("SELECT p FROM Payment p WHERE p.member.memberId = :memberId ORDER BY p.paymentDate DESC")
    List<Payment> findByMemberMemberIdOrderByPaymentDateDesc(@Param("memberId") String memberId);

    List<Payment> findByPaymentStatusOrderByPaymentDateDesc(PaymentStatus status);

    @Query("SELECT SUM(p.finalAmount) FROM Payment p WHERE p.paymentStatus = 'PAID'")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT SUM(p.finalAmount) FROM Payment p WHERE p.paymentStatus = 'PAID' AND p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    BigDecimal calculateRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT FUNCTION('YEAR', p.paymentDate), FUNCTION('MONTH', p.paymentDate), SUM(p.finalAmount) " +
           "FROM Payment p WHERE p.paymentStatus = 'PAID' " +
           "GROUP BY FUNCTION('YEAR', p.paymentDate), FUNCTION('MONTH', p.paymentDate) " +
           "ORDER BY FUNCTION('YEAR', p.paymentDate) DESC, FUNCTION('MONTH', p.paymentDate) DESC")
    List<Object[]> findMonthlyRevenueGrouped();

    @Query("SELECT FUNCTION('DATE', p.paymentDate), SUM(p.finalAmount) " +
           "FROM Payment p WHERE p.paymentStatus = 'PAID' AND p.paymentDate >= :startDate " +
           "GROUP BY FUNCTION('DATE', p.paymentDate) " +
           "ORDER BY FUNCTION('DATE', p.paymentDate) ASC")
    List<Object[]> findDailyRevenueSince(@Param("startDate") LocalDateTime startDate);
}
