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

    private Long sourceId;

    private String title;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Boolean hasRead;

    public Notification(BaseStatus status,
                        Long memberId,
                        Long sourceId,
                        String title,
                        NotificationType type,
                        Boolean hasRead) {
        super(status);
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.title = title;
        this.type = type;
        this.hasRead = hasRead;
    }

}
