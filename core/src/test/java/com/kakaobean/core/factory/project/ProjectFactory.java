package com.kakaobean.core.factory.project;

import com.kakaobean.core.project.domain.Project;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.kakaobean.core.common.domain.BaseStatus.*;

public class ProjectFactory {

    private ProjectFactory() {}

    private static AtomicLong i = new AtomicLong(500);

    public static Project create(){
        return new Project(ACTIVE, 1L, "Test Project", "Project description", "dd361ee9-2381-44be-af12-5b23f265d199");
    }

    public static Project createWithoutId(){
        return new Project("Test Project", "Project description", ACTIVE, "dd361ee9-2381-44be-af12-5b23f265d199" + i.getAndIncrement());
    }

    public static Long getProjectId() {
         return i.getAndIncrement();
    }
}
