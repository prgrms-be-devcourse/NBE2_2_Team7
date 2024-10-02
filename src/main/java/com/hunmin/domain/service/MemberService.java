package com.hunmin.domain.service;

import com.hunmin.domain.dto.member.MemberDTO;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberLevel;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원 가입
    public void registerProcess(MemberDTO memberDTO) {
        String email = memberDTO.getEmail();
        String password = memberDTO.getPassword();

        boolean isExist = memberRepository.existsByEmail(email);

        if (isExist) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 사용자 레벨이 null이면 값 채우기
        MemberLevel selectedLevel = memberDTO.getLevel() != null ? memberDTO.getLevel() : MemberLevel.BEGINNER;

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(memberDTO.getNickname())
                .country(memberDTO.getCountry())
                .memberRole(MemberRole.USER)
                .level(selectedLevel)
                .build();

        memberRepository.save(member);
    }

    // 회원 정보 업데이트
    public void updateMember(Long id, MemberDTO memberDTO) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
                member.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));
            }
            if (memberDTO.getNickname() != null) {
                member.setNickname(memberDTO.getNickname());
            }
            if (memberDTO.getCountry() != null) {
                member.setCountry(memberDTO.getCountry());
            }
            if (memberDTO.getLevel() != null) {
                member.setLevel(MemberLevel.BEGINNER);
            }

            memberRepository.save(member);
        } else {
            throw new RuntimeException("회원 정보를 찾을 수 없습니다.");
        }
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
