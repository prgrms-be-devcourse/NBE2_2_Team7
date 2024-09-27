package com.hunmin.domain.service;

import com.hunmin.domain.dto.notice.*;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.entity.Notice;
import com.hunmin.domain.exception.NoticeException;
import com.hunmin.domain.repository.MemberRepository;
import com.hunmin.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public List<NoticeResponseDTO> getAllNotices(NoticePageRequestDTO pageRequestDTO){
        try {
            Sort sort = Sort.by("noticeId").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            List<Notice> notices = noticeRepository.findAllNoticesResponse(pageable);
            List<NoticeResponseDTO> responseDTOs = new ArrayList<>();
            for (Notice notice : notices) {
                responseDTOs.add(new NoticeResponseDTO(notice));
            }
            return responseDTOs;
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
    public NoticeResponseDTO createNotice(NoticeRequestDTO noticeRequestDTO){
        Member member = getMember(noticeRequestDTO.getMemberId());

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
    public NoticeResponseDTO updateNotice(NoticeUpdateDTO noticeUpdateDTO){
        Member member = getMember(noticeUpdateDTO.getMemberId());
        //관리자 아닐경우 예외 발생
        if (!member.getMemberRole().equals(MemberRole.ADMIN)) {
            throw NoticeException.MEMBER_NOT_VALID.get();
        }
        Notice notice = noticeRepository.findById(noticeUpdateDTO.getNoticeId()).orElseThrow(NoticeException.NOTICE_NOT_FOUND::get);

        try{
            notice.changeTitle(noticeUpdateDTO.getTitle());
            notice.changeContent(noticeUpdateDTO.getContent());
            return new NoticeResponseDTO(notice);
        }catch (Exception e){
            log.error("updateNotice error: {}",  e.getMessage());
            throw NoticeException.NOTICE_NOT_UPDATED.get();
        }
    }



    //공지사항 삭제
    public boolean deleteNotice(NoticeDeleteDTO noticeDeleteDTO){
        Member member = getMember(noticeDeleteDTO.getMemberId());
        //관리자 아닐경우 예외 발생
        if (!member.getMemberRole().equals(MemberRole.ADMIN)) {
            throw NoticeException.MEMBER_NOT_VALID.get();
        }
        noticeRepository.findById(noticeDeleteDTO.getNoticeId()).orElseThrow(NoticeException.NOTICE_NOT_FOUND::get);
        try {
            noticeRepository.deleteById(noticeDeleteDTO.getNoticeId());
            return true;
        }catch (Exception e) {
            log.error("deleteNotice error: {}",  e.getMessage());
            throw NoticeException.NOTICE_NOT_DELETED.get();
        }
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
    }
}
