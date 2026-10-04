package com.gymmanagement.repository;

import com.gymmanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByDateOrderByCheckInTimeDesc(LocalDate date);

    List<Attendance> findByMemberIdOrderByCheckInTimeDesc(Long memberId);

    @Query("SELECT a FROM Attendance a WHERE a.member.memberId = :memberId ORDER BY a.checkInTime DESC")
    List<Attendance> findByMemberMemberIdOrderByCheckInTimeDesc(@Param("memberId") String memberId);

    @Query("SELECT a FROM Attendance a WHERE a.member.id = :memberId AND a.date = :date AND a.checkOutTime IS NULL")
    Optional<Attendance> findOpenAttendanceForMemberOnDate(@Param("memberId") Long memberId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.member.memberId = :memberId AND a.date = :date AND a.checkOutTime IS NULL")
    Optional<Attendance> findOpenAttendanceForMemberIdOnDate(@Param("memberId") String memberId, @Param("date") LocalDate date);

    Boolean existsByMemberIdAndDate(Long memberId, LocalDate date);

    long countByDate(LocalDate date);

    @Query("SELECT a.date, COUNT(a) FROM Attendance a WHERE a.date >= :startDate GROUP BY a.date ORDER BY a.date ASC")
    List<Object[]> findAttendanceTrendSince(@Param("startDate") LocalDate startDate);
}
