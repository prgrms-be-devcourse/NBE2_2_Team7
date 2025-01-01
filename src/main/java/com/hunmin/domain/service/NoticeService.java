package com.hunmin.domain.service;

import com.hunmin.domain.dto.notice.*;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.entity.Notice;
import com.hunmin.domain.exception.MemberException;
import com.hunmin.domain.exception.NoticeException;
import com.hunmin.domain.repository.MemberRepository;
import com.hunmin.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    //공지사항 리스트 조회
    public Page<NoticeResponseDTO> getAllNotices(NoticePageRequestDTO pageRequestDTO){
        try {
            Sort sort = Sort.by("noticeId").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            Page<Notice> noticePage = noticeRepository.findAllNoticesResponse(pageable);
            List<NoticeResponseDTO> responseDTOs = new ArrayList<>();
            // Notice를 NoticeResponseDTO로 변환
            return noticePage.map(NoticeResponseDTO::new);
        }catch (Exception e){
            log.error("getAllNotices error: {}",  e.getMessage());
            throw NoticeException.NOTICE_NOT_FOUND.get();
        }
    }

    //공지사항 조회
    public NoticeResponseDTO getNoticeById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NoticeException.NOTICE_NOT_FOUND::get);
        return new NoticeResponseDTO(notice);
    }

    //공지사항 등록
    public NoticeResponseDTO createNotice(NoticeRequestDTO noticeRequestDTO, String username){
        Member member = getMember(username);

        //관리자 아닐경우 예외 발생
        if (!member.getMemberRole().equals(MemberRole.ADMIN)) {
            throw NoticeException.MEMBER_NOT_VALID.get();
        }
            try {
                Notice notice = noticeRequestDTO.toEntity(member);
                Notice savedNotice = noticeRepository.save(notice);
                log.info("Notice created successfully. Notice ID: {}", savedNotice.getNoticeId());
                return new NoticeResponseDTO(savedNotice);
            }catch (Exception e) {
                log.error("createNotice error: {}",  e.getMessage());
                throw NoticeException.NOTICE_NOT_CREATED.get();
            }
    }

    //공지사항 수정
    public NoticeResponseDTO updateNotice(NoticeUpdateDTO noticeUpdateDTO, String username, Long noticeId){
        Member member = getMember(username);
        //관리자 아닐경우 예외 발생
        if (!member.getMemberRole().equals(MemberRole.ADMIN)) {
            throw NoticeException.MEMBER_NOT_VALID.get();
        }
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NoticeException.NOTICE_NOT_FOUND::get);

        try{
            notice.changeTitle(noticeUpdateDTO.getTitle());
            notice.changeContent(noticeUpdateDTO.getContent());
            notice.changeMember(member); //수정한 관리자에 대한 정보 반영
            return new NoticeResponseDTO(notice);
        }catch (Exception e){
            log.error("updateNotice error: {}",  e.getMessage());
            throw NoticeException.NOTICE_NOT_UPDATED.get();
        }
    }



    //공지사항 삭제
    public boolean deleteNotice(Long noticeId, String username){
        Member member = getMember(username);
        //관리자 아닐경우 예외 발생
        if (!member.getMemberRole().equals(MemberRole.ADMIN)) {
            throw NoticeException.MEMBER_NOT_VALID.get();
        }
        noticeRepository.findById(noticeId).orElseThrow(NoticeException.NOTICE_NOT_FOUND::get);
        try {
            noticeRepository.deleteById(noticeId);
            return true;
        }catch (Exception e) {
            log.error("deleteNotice error: {}",  e.getMessage());
            throw NoticeException.NOTICE_NOT_DELETED.get();
        }
    }

    private Member getMember(String username) {
        Member member = memberRepository.findByEmail(username);
        if (member == null) {
            throw MemberException.NOT_FOUND.get();
        }
        return member;
    }
}
