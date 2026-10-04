package com.gymmanagement.util;

import com.gymmanagement.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberIdGenerator {

    @Autowired
    private MemberRepository memberRepository;

    public synchronized String generateNextMemberId() {
        String maxMemberId = memberRepository.findMaxMemberId();
        if (maxMemberId == null || maxMemberId.trim().isEmpty()) {
            return "GM00001";
        }

        try {
            String numericPart = maxMemberId.substring(2);
            long nextNum = Long.parseLong(numericPart) + 1;
            return String.format("GM%05d", nextNum);
        } catch (Exception e) {
            long count = memberRepository.count() + 1;
            return String.format("GM%05d", count);
        }
    }
}
