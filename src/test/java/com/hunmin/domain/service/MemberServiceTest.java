//package com.hunmin.domain.service;
//
//import com.hunmin.domain.dto.member.MemberDTO;
//import com.hunmin.domain.entity.Member;
//import com.hunmin.domain.repository.MemberRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class MemberServiceTest {
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    void testFindById() {
//        Long memberId = 1L;
//
//        MemberDTO memberDTO = memberService.findById(memberId);
//
//        assertNotNull(memberDTO);
//        assertEquals("USER1@email.com", memberDTO.getEmail());
//    }
//
//    @Test
//    void testCreateMember() {
//
//        MemberDTO memberDTO = new MemberDTO();
//        memberDTO.setMemberId(51L);
//        memberDTO.setEmail("USER51@email.com");
//        memberDTO.setPassword("1234");
//        memberDTO.setNickname("USER51");
//        memberDTO.setCountry("Thailand");
//        memberDTO.setLevel("Expert");
//        memberDTO.setImage("profile.png");
//
//        MemberDTO createdMember = memberService.createMember(memberDTO);
//
//        assertNotNull(createdMember);
//        assertEquals("USER51@email.com", createdMember.getEmail());
//    }
//
//    @Test
//    void testUpdateMember() {
//        Long memberId = 1L;
//        MemberDTO memberDTO = new MemberDTO();
//        memberDTO.setNickname("MEMBER51");
//        memberDTO.setCountry("Kyrgyzstan");
//        memberDTO.setLevel("Native");
//        memberDTO.setImage("profile51.png");
//
//        MemberDTO updatedMember = memberService.updateMember(memberId, memberDTO);
//
//        assertNotNull(updatedMember);
//        assertEquals("MEMBER51", updatedMember.getNickname());
//        assertEquals("Kyrgyzstan", updatedMember.getCountry());
//        assertEquals("Native", updatedMember.getLevel());
//        assertEquals("profile51.png", updatedMember.getImage());
//    }
//
//    @Test
//    void testDeleteMember() {
//        Long memberId = 1L;
//
//        memberService.deleteMember(memberId);
//
//        Optional<Member> deletedMember = memberRepository.findById(memberId);
//        assertTrue(deletedMember.isEmpty());
//    }
//}