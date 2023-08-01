package com.kakaobean.sprint.dto.request;

import com.kakaobean.core.sprint.application.dto.ChangeWorkStatusRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeWorkStatusRequest {

    private String workStatus;

    public ChangeWorkStatusRequest(String workStatus) {
        this.workStatus = workStatus;
    }

    public ChangeWorkStatusRequestDto toServiceDto(Long memberId, Long taskId){
        return new ChangeWorkStatusRequestDto(
                memberId,
                taskId,
                workStatus
        );
    }
}
