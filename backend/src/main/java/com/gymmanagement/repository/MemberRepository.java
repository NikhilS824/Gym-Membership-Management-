package com.gymmanagement.repository;

import com.gymmanagement.entity.Member;
import com.gymmanagement.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Boolean existsByMemberId(String memberId);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);

    @Query("SELECT m FROM Member m WHERE " +
           "(:query IS NULL OR :query = '' OR " +
           "LOWER(m.memberId) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.phone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.email) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR m.status = :status)")
    Page<Member> searchMembers(@Param("query") String query, @Param("status") MemberStatus status, Pageable pageable);

    @Query("SELECT MAX(m.memberId) FROM Member m WHERE m.memberId LIKE 'GM%'")
    String findMaxMemberId();

    List<Member> findByStatus(MemberStatus status);

    long countByStatus(MemberStatus status);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.joiningDate >= CURRENT_DATE - 30 DAY")
    long countNewMembersLast30Days();
}
