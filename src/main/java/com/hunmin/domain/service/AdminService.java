package com.hunmin.domain.service;

import com.hunmin.domain.dto.member.MemberDTO;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.exception.MemberException;
import com.hunmin.domain.exception.NoticeException;
import com.hunmin.domain.repository.BoardRepository;
import com.hunmin.domain.repository.CommentRepository;
import com.hunmin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class AdminService {

    private BoardRepository boardRepository;
    private CommentRepository commentRepository;
    private MemberRepository memberRepository;

    public MemberDTO getMemberByNickname(String username) {
        try {
            Member member = memberRepository.findByNickname(username).orElseThrow(MemberException.NOT_FOUND::get);
            return new MemberDTO(member);
        } catch (Exception e) {
            log.error("getMemberByNickname : {}",e.getMessage());
            throw MemberException.NOT_FOUND.get();
        }
    }


}
