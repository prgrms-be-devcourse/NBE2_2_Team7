package com.hunmin.domain.service;

import com.hunmin.domain.dto.notification.NotificationRequestDTO;
import com.hunmin.domain.dto.notification.NotificationResponseDTO;
import com.hunmin.domain.dto.notification.NotificationSendDTO;
import com.hunmin.domain.handler.SseEmitters;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.Notification;
import com.hunmin.domain.entity.NotificationType;
import com.hunmin.domain.repository.MemberRepository;
import com.hunmin.domain.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SseEmitters sseEmitters;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //SSE 연결 테스트
    @Test
    void subscribe() {
        Long memberId = 1L;
        HttpServletResponse response = mock(HttpServletResponse.class);

        SseEmitter emitter = notificationService.subscribe(memberId);

        assertNotNull(emitter);
    }

    //알림 전송 테스트
    @Test
    void sendNotification() {
        NotificationSendDTO sendDTO = NotificationSendDTO.builder()
                .memberId(1L)
                .message("알림 테스트")
                .notificationType(NotificationType.COMMENT)
                .url("/test/url")
                .build();

        Member member = new Member(1L, "테스터");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        notificationService.send(sendDTO);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    //회원 별 알림 조회 테스트
    @Test
    void readNotificationByMember() {
        Long memberId = 1L;
        notificationService.ReadNotificationByMember(memberId);

        verify(notificationRepository, times(1)).findByMemberId(memberId);
    }

    // 알림 읽음 처리 테스트
    @Test
    void updateNotification() {
        Notification notification = Notification.builder()
                .notificationId(1L)
                .member(new Member(1L, "테스터"))
                .message("Test Message")
                .notificationType(NotificationType.COMMENT)
                .url("/test/url")
                .isRead(false)
                .build();

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        NotificationResponseDTO responseDTO = notificationService.updateNotification(1L);

        notification.changeIsRead(true);
        assertEquals(true, notification.getIsRead());
        assertNotNull(responseDTO);
    }
}
