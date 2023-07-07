package com.kakaobean.core.releasenote.domain;


import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.releasenote.exception.ReleaseNoteWriterAccessException;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.project.domain.ProjectRole.*;

@Component
public class ReleaseNoteValidator {

    public void validWriterAccess(ProjectMember writer){
        ProjectRole role = writer.getProjectRole();
        if(role != ADMIN){
            throw new ReleaseNoteWriterAccessException();
        }
    }
}
