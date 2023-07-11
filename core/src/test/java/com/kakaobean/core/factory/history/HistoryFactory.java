package com.kakaobean.core.factory.history;

import com.kakaobean.core.releasenote.domain.History;

public class HistoryFactory {

    public HistoryFactory() {}

    public static History create(Long releaseNoteId){
        return History.builder()
                .id(1L)
                .releaseNoteId(releaseNoteId)
                .build();
    }
}
