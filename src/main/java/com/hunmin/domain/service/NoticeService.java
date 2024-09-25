package com.hunmin.domain.service;

import com.hunmin.domain.dto.notice.NoticeRequestDTO;
import com.hunmin.domain.dto.notice.NoticeResponseDTO;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.entity.Notice;
import com.hunmin.domain.exception.NoticeException;
import com.hunmin.domain.exception.NoticeTaskException;
import com.hunmin.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
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

    public List<NoticeResponseDTO> getAllNotices(PageRequestDTO pageRequestDTO, Long noticeId){
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> NoticeException.NOT_FOUND_NOTICE.get());

        Sort sort = Sort.by("noticeId").descending();
        Pageable pageable = pageRequestDTO.getPageable(sort);
        Page<Notice> notices = noticeRepository.findAll(pageable);
        return noticeRepository.getAllNoticesResponse(pageable);
    }

    public NoticeResponseDTO getNoticeById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> NoticeException.NOT_FOUND_NOTICE.get());
        return new NoticeResponseDTO(notice);
    }

    public NoticeResponseDTO createNotice(NoticeRequestDTO noticeRequestDTO){
        if(noticeRequestDTO.getMember().getMemberRole().equals(MemberRole.USER)) {
            throw NoticeException.MEMBER_NOT_VALID.get();

        }
    }


}
