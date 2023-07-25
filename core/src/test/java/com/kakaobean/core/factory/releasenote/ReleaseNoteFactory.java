package com.kakaobean.core.factory.releasenote;

import com.kakaobean.core.releasenote.domain.ReleaseNote;

public class ReleaseNoteFactory {

    private ReleaseNoteFactory() {}

    public static ReleaseNote createWithId(Long memberId, Long projectId){
        return ReleaseNote.builder()
                .title("릴리즈노트 제목")
                .content("릴리즈노트 내용")
                .version("1.3")
                .memberId(memberId)
                .projectId(projectId)
                .build();
    }
}
