package com.gymmanagement.repository;

import com.gymmanagement.entity.Membership;
import com.gymmanagement.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findByMemberIdOrderByStartDateDesc(Long memberId);

    @Query("SELECT m FROM Membership m WHERE m.member.memberId = :memberId ORDER BY m.startDate DESC")
    List<Membership> findByMemberMemberIdOrderByStartDateDesc(@Param("memberId") String memberId);

    @Query("SELECT m FROM Membership m WHERE m.member.id = :memberId AND m.status = 'ACTIVE' AND :currentDate BETWEEN m.startDate AND m.endDate ORDER BY m.endDate DESC")
    Optional<Membership> findActiveMembershipForMember(@Param("memberId") Long memberId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT m FROM Membership m WHERE m.member.memberId = :memberId AND m.status = 'ACTIVE' AND :currentDate BETWEEN m.startDate AND m.endDate ORDER BY m.endDate DESC")
    Optional<Membership> findActiveMembershipByMemberId(@Param("memberId") String memberId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT m FROM Membership m WHERE m.status = 'ACTIVE' AND m.endDate >= :startDate AND m.endDate <= :endDate ORDER BY m.endDate ASC")
    List<Membership> findExpiringMemberships(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(m) FROM Membership m WHERE m.status = 'ACTIVE' AND :currentDate BETWEEN m.startDate AND m.endDate")
    long countActiveMemberships(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT COUNT(m) FROM Membership m WHERE m.endDate < :currentDate OR m.status = 'EXPIRED'")
    long countExpiredMemberships(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT COUNT(m) FROM Membership m WHERE m.status = 'ACTIVE' AND m.endDate BETWEEN :today AND :expiryLimit")
    long countExpiringSoonMemberships(@Param("today") LocalDate today, @Param("expiryLimit") LocalDate expiryLimit);

    @Query("SELECT m.plan.name, COUNT(m) FROM Membership m GROUP BY m.plan.name ORDER BY COUNT(m) DESC")
    List<Object[]> findPopularPlansCount();
}
