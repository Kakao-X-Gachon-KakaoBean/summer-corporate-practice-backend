package com.kakaobean.core.issue.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindIssuesWithinPageResponseDto {
    private final boolean finalPage;
    private final List<IssueDto> issues;

    public FindIssuesWithinPageResponseDto(boolean finalPage, List<IssueDto> issues) {
        this.finalPage = finalPage;
        this.issues = issues;
    }

    @Getter
    public static class IssueDto {

        private final Long id;
        private final String title;
        private final Long writerId;
        private final String writerName;
        private final String writtenTime;

        public IssueDto(Long id, String title, Long writerId, String writerName, String writtenTime) {
            this.id = id;
            this.title = title;
            this.writerId = writerId;
            this.writerName = writerName;
            this.writtenTime = writtenTime;
        }
    }
}
