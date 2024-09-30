package com.hunmin.domain.service;

import com.hunmin.domain.dto.member.MemberDTO;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    // 회원 id로 조회
//    public MemberDTO findById(Long memberId) {
//        Optional<Member> foundMember = memberRepository.findById(memberId);
//        Member member = foundMember.orElseThrow(MemberException.BAD_CREDENTIALS::get);
//        return new MemberDTO(member);
//    }

//    // 회원 id, pw로 조회
//    public MemberDTO findById(Long memberId, String password) {
//        Optional<Member> foundMember = memberRepository.findById(memberId);
//        Member member = foundMember.orElseThrow(MemberException.BAD_CREDENTIALS::get);
//
//        if (!passwordEncoder.matches(password, member.getPassword())) {
//            throw MemberException.BAD_CREDENTIALS.get();
//        }
//        return new MemberDTO(member);
//    }

//    // 회원 가입
//    public MemberDTO createMember(MemberDTO memberDTO) {
//        Member member = Member.builder()
//                .email(memberDTO.getEmail())
//                .password(passwordEncoder.encode(memberDTO.getPassword()))
//                .nickname(memberDTO.getNickname())
//                .country(memberDTO.getCountry())
//                .level(memberDTO.getLevel())
//                .image(memberDTO.getImage())
//                .memberRole(MemberRole.USER)
//                .build();
//
//        boolean isExist = memberRepository.existsByEmail(member.getEmail());
//        if (isExist) {
//            throw new MemberTaskException("이미 존재하는 회원입니다", 409);
//        }
//
//        Member savedMember = memberRepository.save(member);
//        return new MemberDTO(savedMember);
//    }

//    // 회원 정보 수정
//    public MemberDTO updateMember(Long memberId, MemberDTO memberDTO) {
//        Member foundMember = memberRepository.findById(memberId).orElseThrow(() ->
//                new MemberTaskException("회원이 존재하지 않습니다.", 404));
//
//        foundMember.changePassword(memberDTO.getPassword());
//        foundMember.changeNickname(memberDTO.getNickname());
//        foundMember.changeCountry(memberDTO.getCountry());
//        foundMember.changeLevel(memberDTO.getLevel());
//        foundMember.changeImage(memberDTO.getImage());
//
//        // 비밀번호가 존재하면 업데이트
//        if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
//            foundMember.changePassword(memberDTO.getPassword());
//        }
//
//        Member updatedMember = memberRepository.save(foundMember);
//        return new MemberDTO(updatedMember);
//    }
//
//    // 회원 정보 삭제
//    public void deleteMember(Long memberId) {
//        Member foundMember = memberRepository.findById(memberId).orElseThrow(() ->
//                new MemberTaskException("회원이 존재하지 않습니다.", 404));
//        memberRepository.delete(foundMember);
//    }
    
    // 회원 가입
    public void joinProcess(MemberDTO memberDTO) {
        String email = memberDTO.getEmail();
        String password = memberDTO.getPassword();

        boolean isExist = memberRepository.existsByEmail(email);

        if (isExist) {
            return;
        }

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(memberDTO.getNickname())
                .country(memberDTO.getCountry())
                .level(memberDTO.getLevel())
                .image(memberDTO.getImage())
                .memberRole(MemberRole.ADMIN)
                .build();

        memberRepository.save(member);
    }

}
