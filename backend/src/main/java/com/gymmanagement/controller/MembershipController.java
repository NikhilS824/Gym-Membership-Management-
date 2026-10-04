package com.gymmanagement.controller;

import com.gymmanagement.dto.request.MembershipRequest;
import com.gymmanagement.dto.request.RenewRequest;
import com.gymmanagement.dto.response.MembershipResponse;
import com.gymmanagement.dto.response.VerificationResponse;
import com.gymmanagement.service.MembershipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping("/api/memberships")
    public ResponseEntity<List<MembershipResponse>> getAllMemberships() {
        return ResponseEntity.ok(membershipService.getAllMemberships());
    }

    @GetMapping("/api/memberships/member/{memberId}")
    public ResponseEntity<List<MembershipResponse>> getMembershipsByMemberId(@PathVariable String memberId) {
        return ResponseEntity.ok(membershipService.getMembershipsByMemberId(memberId));
    }

    @GetMapping("/api/memberships/{id}")
    public ResponseEntity<MembershipResponse> getMembershipById(@PathVariable Long id) {
        return ResponseEntity.ok(membershipService.getMembershipById(id));
    }

    @PostMapping("/api/memberships")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<MembershipResponse> createMembership(@Valid @RequestBody MembershipRequest request) {
        MembershipResponse response = membershipService.createMembership(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/api/memberships/{id}/renew")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<MembershipResponse> renewMembership(@PathVariable Long id, @Valid @RequestBody RenewRequest request) {
        MembershipResponse response = membershipService.renewMembership(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/membership/verify/{memberId}")
    public ResponseEntity<VerificationResponse> verifyMembership(@PathVariable String memberId) {
        VerificationResponse response = membershipService.verifyMembership(memberId);
        return ResponseEntity.ok(response);
    }
}
