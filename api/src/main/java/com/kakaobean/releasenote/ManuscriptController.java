package com.kakaobean.releasenote;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.releasenote.dto.request.RegisterManuscriptRequest;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManuscriptController {

    private final ManuscriptService manuscriptService;

    @PostMapping("/manuscripts")
    public ResponseEntity registerManuscript(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @RequestBody @Validated RegisterManuscriptRequest request){
        manuscriptService.registerManuscript(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("릴리즈 노트 원고 등록에 성공했습니다."), HttpStatus.CREATED);
    }
}
