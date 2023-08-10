package com.kakaobean.core.issue.domain.repository.query;

import com.kakaobean.core.issue.domain.Issue;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FindIndividualIssueResponseDto {

    private IssueDto issue;

    private List<CommentDto> comments;

    public FindIndividualIssueResponseDto(IssueDto issue, List<CommentDto> comments) {
        this.issue = issue;
        this.comments = comments;
    }

    @Getter
    public static class IssueDto {

        private Long issueId;
        private String title;
        private String content;
        private String writtenTime;
        private String writerName;
        private String thumbnailImg;

        public IssueDto(Long issueId, String title, String content, String writtenTime, String writerName, String thumbnailImg) {
            this.issueId = issueId;
            this.title = title;
            this.content = content;
            this.writtenTime = writtenTime;
            this.writerName = writerName;
            this.thumbnailImg = thumbnailImg;
        }
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
