package com.hunmin.domain.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
    @NotBlank
    private Long notificationId;

    @NotBlank
    private Long memberId;
}
