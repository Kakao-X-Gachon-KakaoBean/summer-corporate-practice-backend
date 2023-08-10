package com.kakaobean.core.issue.domain.repository.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FindIndividualIssueResponseDto {

    private Long issueId;
    private String title;
    private String content;
    private String writtenTime;
    private String writerName;
    private String thumbnailImg;
    private List<CommentDto> comments = new ArrayList<>();

    public FindIndividualIssueResponseDto(Long issueId, String title, String content, String writtenTime, String writerName, String thumbnailImg) {
        this.issueId = issueId;
        this.title = title;
        this.content = content;
        this.writtenTime = writtenTime;
        this.writerName = writerName;
        this.thumbnailImg = thumbnailImg;
    }
    public FindIndividualIssueResponseDto(Long issueId, String title, String content, String writtenTime, String writerName, String thumbnailImg, List<CommentDto> comments) {
        this.issueId = issueId;
        this.title = title;
        this.content = content;
        this.writtenTime = writtenTime;
        this.writerName = writerName;
        this.thumbnailImg = thumbnailImg;
        this.comments = comments;
    }

    @Getter
    public static class CommentDto {

        private final Long commentId;

        private final String content;

        private final String writtenTime;

        private final String writerName;

        private final String thumbnailImg;


        public CommentDto(Long commentId, String content, String writtenTime, String writerName, String thumbnailImg) {
            this.commentId = commentId;
            this.content = content;
            this.writtenTime = writtenTime;
            this.writerName = writerName;
            this.thumbnailImg = thumbnailImg;
        }
    }
}
