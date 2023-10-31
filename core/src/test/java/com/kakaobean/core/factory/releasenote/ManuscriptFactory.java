package com.kakaobean.core.factory.releasenote;

import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.application.dto.request.ModifyManuscriptRequestDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.ManuscriptStatus;

import java.util.concurrent.atomic.AtomicInteger;

import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.releasenote.domain.ManuscriptStatus.*;

public class ManuscriptFactory {

    private ManuscriptFactory() {}

    private static AtomicInteger i = new AtomicInteger(0);

    public static Manuscript create(){
        int i = getAtomicInteger();
        return new Manuscript(ACTIVE, "manuscript title", "c..", "3." + i, 1L, 2L, Modifiable);
    }

    public static Manuscript create(ManuscriptStatus status){
        return new Manuscript(ACTIVE, "manuscript title", "c..", "3." + i, 1L, 2L, status);
    }

    public static Manuscript createWithId(Long memberId, Long projectId){
        return new Manuscript(ACTIVE, "manuscript title", "c..", "3." + i, memberId, projectId, Modifiable);
    }

    public static Manuscript createWithId(Long memberId, Long projectId, ManuscriptStatus status){
        return new Manuscript(ACTIVE, "manuscript title", "c..", "3." + i, memberId, projectId, status);
    }

    public static ModifyManuscriptRequestDto createServiceDto(Long memberId, Long manuscriptId){
        return new ModifyManuscriptRequestDto("3.3V edit title", "edit content", "3.3V", memberId, manuscriptId);
    }

    private static int getAtomicInteger() {
        return i.getAndIncrement();
    }
}
