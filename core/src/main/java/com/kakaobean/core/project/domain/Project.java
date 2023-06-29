package com.kakaobean.core.project.domain;

import com.kakaobean.core.common.domain.BaseEntity;
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

    protected Project() {}

    public Project(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
