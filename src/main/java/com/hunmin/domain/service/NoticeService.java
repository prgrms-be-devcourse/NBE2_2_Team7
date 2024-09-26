package com.hunmin.domain.service;

import com.hunmin.domain.dto.notice.NoticeRequestDTO;
import com.hunmin.domain.dto.notice.NoticeResponseDTO;
import com.hunmin.domain.dto.notice.NoticeUpdateDTO;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.entity.Notice;
import com.hunmin.domain.exception.NoticeException;
import com.hunmin.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class NoticeService {
    private final NoticeRepository noticeRepository;

    //공지사항 리스트 조회
    public List<NoticeResponseDTO> getAllNotices(PageRequestDTO pageRequestDTO, Long noticeId){
            Notice notice = noticeRepository.findById(noticeId).orElseThrow(NoticeException.NOTICE_NOT_FOUND::get);
        try {
            Sort sort = Sort.by("noticeId").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);;
            return noticeRepository.getAllNoticesResponse(pageable);
        }catch (Exception e){
            log.error("getAllNotices error: {}",  e);
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
        //관리자 아닐경우 예외 발생
        if (!noticeRequestDTO.getMember().getMemberRole().equals(MemberRole.ADMIN)) {
            throw NoticeException.MEMBER_NOT_VALID.get();
        }
            try {
                Notice notice = noticeRequestDTO.toEntity();
                Notice savedNotice = noticeRepository.save(notice);
                return new NoticeResponseDTO(savedNotice);
            }catch (Exception e) {
                log.error("createNotice error: {}",  e);
                throw NoticeException.NOTICE_NOT_CREATED.get();
            }
    }

    //공지사항 수정
    public NoticeResponseDTO updateNotice(NoticeUpdateDTO noticeUpdateDTO){
        //관리자 아닐경우 예외 발생
        if (!noticeUpdateDTO.getMember().getMemberRole().equals(MemberRole.ADMIN)) {
            throw NoticeException.MEMBER_NOT_VALID.get();
        }
        Notice notice = noticeRepository.findById(noticeUpdateDTO.getNoticeId()).orElseThrow(NoticeException.NOTICE_NOT_FOUND::get);

        try{
            notice.changeTitle(noticeUpdateDTO.getTitle());
            notice.changeContent(noticeUpdateDTO.getContent());
            notice.changeMember(noticeUpdateDTO.getMember());
            return new NoticeResponseDTO(notice);
        }catch (Exception e){
            log.error("updateNotice error: {}",  e);
            throw NoticeException.NOTICE_NOT_UPDATED.get();
        }
    }

    //공지사항 삭제
    public NoticeResponseDTO deleteNotice(Notice noticeDeleteDTO){
        //관리자 아닐경우 예외 발생
        if (!noticeDeleteDTO.getMember().getMemberRole().equals(MemberRole.ADMIN)) {
            throw NoticeException.MEMBER_NOT_VALID.get();
        }
        Notice notice = noticeRepository.findById(noticeDeleteDTO.getNoticeId()).orElseThrow(NoticeException.NOTICE_NOT_FOUND::get);
        try {
            noticeRepository.deleteById(noticeDeleteDTO.getNoticeId());
            return new NoticeResponseDTO(notice);
        }catch (Exception e) {
            log.error("deleteNotice error: {}",  e);
            throw NoticeException.NOTICE_NOT_DELETED.get();
        }
    }


}
