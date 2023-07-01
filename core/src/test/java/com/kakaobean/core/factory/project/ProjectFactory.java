package com.kakaobean.core.factory.project;

import com.kakaobean.core.project.domain.Project;

import static com.kakaobean.core.common.domain.BaseStatus.*;

public class ProjectFactory {

    private ProjectFactory() {}

    public static Project create(){
        return new Project(ACTIVE, 1L, "Test Project", "Project description", "dd361ee9-2381-44be-af12-5b23f265d199");
    }
}
