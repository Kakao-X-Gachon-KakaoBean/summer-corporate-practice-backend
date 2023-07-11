package com.kakaobean.core.releasenote.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
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
@SQLDelete(sql = "UPDATE history SET status = INACTIVE WHERE id = ?")
@Entity
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long releaseNoteId;

    /**
     * 테스트용
     */
    @Builder
    public History(BaseStatus status,
                   Long id,
                   Long releaseNoteId) {
        super(BaseStatus.ACTIVE);
        this.id = id;
        this.releaseNoteId = releaseNoteId;
    }

    public History() {}
}
