package com.kakaobean.core.issue.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindIndividualIssueResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writtenTime;
    private String writerName;
    private String thumbnailImg;
    private List<CommentDto> comments;

    public FindIndividualIssueResponseDto(Long id, String title, String content, String writtenTime, String writerName, String thumbnailImg, List<CommentDto> comments) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.writtenTime = writtenTime;
            this.writerName = writerName;
            this.thumbnailImg = thumbnailImg;
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
