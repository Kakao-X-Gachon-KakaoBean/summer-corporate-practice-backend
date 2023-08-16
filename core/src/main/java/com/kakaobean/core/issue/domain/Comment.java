package com.kakaobean.core.issue.domain;


import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.issue.domain.event.RegisterCommentEvent;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE comment SET status = 'INACTIVE' WHERE id = ?")
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long issueId;

    private String content;

    private Long writerId;

    protected Comment(){}

    public Comment(BaseStatus status,
                 Long issueId,
                 String content,
                 Long writerId) {
        super(status);
        this.issueId = issueId;
        this.content = content;
        this.writerId = writerId;
    }

    /**
     * 테스트용
     */
    @Builder
    public Comment(BaseStatus status,
                 Long id,
                 Long issueId,
                 String content,
                 Long writerId) {
        super(BaseStatus.ACTIVE);
        this.id = id;
        this.issueId = issueId;
        this.content = content;
        this.writerId = writerId;
    }
    public void registered() {
        Events.raise(new RegisterCommentEvent(id));
    }

    public void modify(String content){
        this.content = content;
    }
}