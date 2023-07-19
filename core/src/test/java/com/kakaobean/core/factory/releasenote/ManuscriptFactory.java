package com.kakaobean.core.factory.releasenote;

import com.kakaobean.core.releasenote.domain.Manuscript;

import static com.kakaobean.core.common.domain.BaseStatus.*;

public class ManuscriptFactory {

    private ManuscriptFactory() {}

    public static Manuscript create(){
        return new Manuscript(ACTIVE, "manuscript title", "c..", "3.1", 1L, 2L);
    }

    public static Manuscript createWithId(Long memberId, Long projectId){
        return new Manuscript(ACTIVE, "manuscript title", "c..", "3.1", memberId, projectId);
    }
}
