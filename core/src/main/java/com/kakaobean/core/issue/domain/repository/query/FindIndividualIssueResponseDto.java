package com.kakaobean.core.issue.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindIndividualIssueResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long writerId;
    private String writerName;
    private String writtenTime;
    private List<CommentDto> comments;

    public FindIndividualIssueResponseDto(Long id, String title, String content, Long writerId, String writerName, String writtenTime, List<CommentDto> comments) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.writerId = writerId;
            this.writerName = writerName;
            this.writtenTime = writtenTime;
            this.comments = comments;
    }

    @Getter
    public static class CommentDto {

        private final Long id;

        private final String content;

        private final Long writerId;

        private final String writerName;

        private final String writtenTime;

        public CommentDto(Long id, String content, Long writerId, String writerName, String writtenTime) {
            this.id = id;
            this.content = content;
            this.writerId = writerId;
            this.writerName = writerName;
            this.writtenTime = writtenTime;
        }
    }
}
