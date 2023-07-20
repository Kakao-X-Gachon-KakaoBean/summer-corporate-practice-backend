package com.kakaobean.core.notification.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE notification SET status = 'INACTIVE' WHERE id = ?")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String url;

    private Boolean hasRead;

    private String content;

    public Notification(BaseStatus status,
                        Long memberId,
                        String url,
                        Boolean hasRead,
                        String content) {
        super(status);
        this.memberId = memberId;
        this.url = url;
        this.hasRead = hasRead;
        this.content = content;
    }
}
