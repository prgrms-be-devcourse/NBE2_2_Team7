package com.hunmin.domain.event;

import com.hunmin.domain.dto.notification.NotificationSendDTO;
import com.hunmin.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {
    private final NotificationService notificationService;

    //알림 전송
    @Async
    @TransactionalEventListener
    public void sendNotification(NotificationSendDTO notificationSendDTO){
        notificationService.send(notificationSendDTO);
    }
}
