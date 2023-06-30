package com.kakaobean.core.project.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String secretKey;

    protected Project() {}

    public Project(String title,
                   String content,
                   BaseStatus baseStatus,
                   String secretKey) {
        super(baseStatus);
        this.title = title;
        this.content = content;
        this.secretKey = secretKey;
    }
}
