package com.hunmin.domain.dto.notification;

import com.hunmin.domain.entity.Notification;
import com.hunmin.domain.entity.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long notificationId;
    private String message;
    private NotificationType notificationType;
    private String url;
    private Boolean isRead;

    public NotificationResponseDTO(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.message = notification.getMessage();
        this.notificationType = notification.getNotificationType();
        this.url = notification.getUrl();
        this.isRead = notification.getIsRead();
    }
}
