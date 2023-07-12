package com.kakaobean.core.factory.releasenote;

import com.kakaobean.core.releasenote.domain.ReleaseNote;

public class ReleaseNoteFactory {

    private ReleaseNoteFactory() {}

    private static ReleaseNote create(Long memberId, Long projectId){
        return ReleaseNote.builder()
                .id(1L)
                .title("릴리즈노트 제목")
                .content("릴리즈노트 내용")
                .version(1.0)
                .memberId(memberId)
                .projectId(projectId)
                .build();
    }
}
