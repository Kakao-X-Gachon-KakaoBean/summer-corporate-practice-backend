package com.kakaobean.core.notification.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindPagingNotificationResponseDto {

    private final boolean finalPage;
    private final List<FindNotificationResponseDto> releaseNotes;

    public FindPagingNotificationResponseDto(boolean finalPage, List<FindNotificationResponseDto> releaseNotes) {
        this.finalPage = finalPage;
        this.releaseNotes = releaseNotes;
    }

}
