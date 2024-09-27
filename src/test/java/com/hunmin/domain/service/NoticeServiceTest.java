package com.hunmin.domain.service;

import com.hunmin.domain.dto.notice.*;
import com.hunmin.domain.dto.page.PageRequestDTO;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.entity.Notice;
import com.hunmin.domain.exception.NoticeException;
import com.hunmin.domain.exception.NoticeTaskException;
import com.hunmin.domain.repository.MemberRepository;
import com.hunmin.domain.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


/*
    테스트 코드는 수정 임시보류.
 */
@SpringBootTest
@Transactional
class NoticeServiceTest {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NoticeService noticeService;

    private Member savedMember;
    private Notice savedNotice;
    private List<Member> savedMembers = new ArrayList<>();
    private List<Notice> savedNotices = new ArrayList<>();

    @BeforeEach
    @DisplayName("더미데이터")
    public void setUp() {
        for(int i=1; i<=50;i++) {
            Member member = Member.builder()
                    .memberRole(i <= 30 ? MemberRole.ADMIN : MemberRole.USER)
                    .nickname(i <= 30 ? "관리자이름" + i : "유저이름" + i)
                    .country(i <= 30 ? "관리자국적" + i : "유저국적" + i)
                    .email(i <= 30 ? "관리자이멜" + i : "유저이멜" + i)
                    .image(i <= 30 ? "관리자아바타" + i : "유저아바타" + i)
                    .level(i <= 30 ? "관리자레벨" : "유저레벨")
                    .password(i <= 30 ? "관리자비밀번호" + i : "유저비밀번호" + i)
                    .build();
            savedMember = memberRepository.save(member);
            savedMembers.add(savedMember);
            // 데이터 저장 확인
            //System.out.println("멤버 Id: " + savedMember.getMemberId() + " - " + savedMember.getNickname());

            if(i<=30){
                Notice notice = Notice.builder()
                        .title("제목" + i)
                        .content("내용" + i)
                        .member(savedMember)
                        .build();
                savedNotice =noticeRepository.save(notice);
                savedNotices.add(savedNotice);
                // 데이터 저장 확인
                //System.out.println("공지사항 Id: " + savedNotice.getNoticeId() + " - " + savedNotice.getTitle());
            }
        }
    }

    @Test
    @DisplayName("공지 리스트 조회")
    void getAllNotices() {
        //given
        NoticePageRequestDTO pageRequestDTO = new NoticePageRequestDTO();

        //when
        List<NoticeResponseDTO> notices = noticeService.getAllNotices(pageRequestDTO);
        //then
        assertThat(notices).isNotEmpty(); //공지  유무 확인
        assertThat(notices).hasSizeLessThanOrEqualTo(savedNotices.size()); //공지 사이즈가 일치한지 확인
    }

    @Test
    @DisplayName("공지 조회")
    void getNoticeById() {
        //given
        Notice findNotice = savedNotices.get(0);
        Long noticeId= findNotice.getNoticeId();

        //when
        NoticeResponseDTO findNoticeResponse = noticeService.getNoticeById(noticeId);

        //then
        assertThat(findNoticeResponse).isNotNull();
        assertThat(findNoticeResponse.getNoticeId()).isEqualTo(noticeId);
        assertThat(findNoticeResponse.getContent()).isEqualTo(findNotice.getContent());
    }

    @Test
    @DisplayName("관리자가 공지사항 등록")
    void createNotice() {
        //given
        Long adminId = savedMembers.get(40).getMemberId();
        NoticeRequestDTO noticeRequestDTO = new NoticeRequestDTO();
        noticeRequestDTO.setTitle("Test Notice");
        noticeRequestDTO.setContent("This is a test notice.");

        //when
        NoticeResponseDTO noticeResponse = noticeService.createNotice(noticeRequestDTO,adminId);

        //then
        assertThat(noticeResponse).isNotNull();
        assertThat(noticeResponse.getTitle()).isEqualTo(noticeRequestDTO.getTitle());
        //DB에 저장되었는지 확인
        NoticeResponseDTO findNoticeById = noticeService.getNoticeById(noticeResponse.getNoticeId());
        assertThat(findNoticeById).isNotNull();
        assertThat(findNoticeById.getNoticeId()).isEqualTo(noticeResponse.getNoticeId());
        assertThat(findNoticeById.getTitle()).isEqualTo(noticeRequestDTO.getTitle());
    }

    @Test
    @DisplayName("유저가 공지사항 등록(예외발생)")
    void createNoticeFail() {
        //when
        Long userId = savedMembers.get(40).getMemberId();
        NoticeRequestDTO noticeRequestDTO = new NoticeRequestDTO();
        noticeRequestDTO.setTitle("공지제목");
        noticeRequestDTO.setContent("공지내용");



        assertThatThrownBy(() -> noticeService.createNotice(noticeRequestDTO,userId))
                .isInstanceOf(NoticeTaskException.class)
                .hasMessage(NoticeException.MEMBER_NOT_VALID.get().getMessage());
    }

    @Test
    @DisplayName("공지사항 수정")
    void updateNotice() {
        //given
        Long adminId = savedMembers.get(1).getMemberId();
        Long noticeId = savedNotices.get(10).getNoticeId();

        NoticeUpdateDTO noticeUpdateDTO = new NoticeUpdateDTO();
        noticeUpdateDTO.setNoticeId(noticeId);
        noticeUpdateDTO.setTitle("제목수정");
        noticeUpdateDTO.setContent("내용수정");


        //when
        NoticeResponseDTO noticeUpdateResponse = noticeService.updateNotice(noticeUpdateDTO,adminId);

        //then
        assertThat(noticeUpdateResponse).isNotNull();
        assertThat(noticeUpdateResponse.getNoticeId()).isEqualTo(noticeUpdateDTO.getNoticeId());
        assertThat(noticeUpdateResponse.getTitle()).isEqualTo(noticeUpdateDTO.getTitle());
        assertThat(noticeUpdateResponse.getContent()).isEqualTo(noticeUpdateDTO.getContent());

        //DB에 저장되었나 확인
        NoticeResponseDTO findNoticeById = noticeService.getNoticeById(noticeUpdateResponse.getNoticeId());
        assertThat(findNoticeById.getTitle()).isEqualTo(noticeUpdateDTO.getTitle());
        assertThat(findNoticeById.getContent()).isEqualTo(noticeUpdateDTO.getContent());

    }

    @Test
    @DisplayName("유저가 공지사항 수정 (예외발생)")
    void updateNoticeFail() {
        //given
        Long userId = savedMembers.get(1).getMemberId();
        Long noticeId = savedNotices.get(10).getNoticeId();


        NoticeUpdateDTO noticeUpdateDTO = new NoticeUpdateDTO();
        noticeUpdateDTO.setNoticeId(noticeId);
        noticeUpdateDTO.setTitle("제목수정");
        noticeUpdateDTO.setContent("내용수정");


        assertThatThrownBy(() -> noticeService.updateNotice(noticeUpdateDTO)
                .isInstanceOf(NoticeTaskException.class)
                .hasMessage(NoticeException.MEMBER_NOT_VALID.get().getMessage());
    }

    @Test
    @DisplayName("관리자만 삭제, 유저가 삭제시 예외발생")
    void deleteNotice() {
        //given
        Member findAdmin = savedMembers.get(10);
        Member findUser = savedMembers.get(40);
        Notice findNotice = savedNotices.get(10);

        //given
        NoticeDeleteDTO adminDeleteDTO = new NoticeDeleteDTO();
        adminDeleteDTO.setNoticeId(findNotice.getNoticeId());
        adminDeleteDTO.setMember(findAdmin);

        NoticeDeleteDTO userDeleteDTO = new NoticeDeleteDTO();
        userDeleteDTO.setNoticeId(findNotice.getNoticeId());
        userDeleteDTO.setMember(findUser);

        //when
        noticeService.deleteNotice(adminDeleteDTO);

        assertThatThrownBy(() -> noticeService.deleteNotice(userDeleteDTO))
                .isInstanceOf(NoticeTaskException.class)
                .hasMessage(NoticeException.MEMBER_NOT_VALID.get().getMessage());

        //then
        assertThat(noticeRepository.findById(findNotice.getNoticeId())).isEmpty();
    }
}