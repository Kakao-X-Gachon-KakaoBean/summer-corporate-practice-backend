package com.kakaobean.core.factory.releasenote;

import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.ManuscriptStatus;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.releasenote.domain.ManuscriptStatus.*;

public class ManuscriptFactory {

    private ManuscriptFactory() {}

    public static Manuscript create(){
        return new Manuscript(ACTIVE, "manuscript title", "c..", "3.1", 1L, 2L, Modifiable);
    }

    public static Manuscript createWithId(Long memberId, Long projectId){
        return new Manuscript(ACTIVE, "manuscript title", "c..", "3.1", memberId, projectId, Modifiable);
    }
}
