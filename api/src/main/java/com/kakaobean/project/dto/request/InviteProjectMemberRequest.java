package com.kakaobean.project.dto.request;

import com.kakaobean.core.project.application.dto.request.InviteProjectMemberRequestDto;
import lombok.Getter;

import java.util.List;

@Getter
public class InviteProjectMemberRequest {

    private List<Long> invitedMemberIdList;

    public InviteProjectMemberRequestDto toServiceDto(){
        return new InviteProjectMemberRequestDto(invitedMemberIdList);
    }

}
