package com.kakaobean.releasenote;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.releasenote.application.ReleaseNoteService;
import com.kakaobean.releasenote.dto.request.RegisterReleaseNoteRequest;
import com.kakaobean.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;


@RestController
public class ReleaseNoteController {

    private final ReleaseNoteService releaseNoteService;

    public ReleaseNoteController(ReleaseNoteService releaseNoteService) {
        this.releaseNoteService = releaseNoteService;
    }

    @PostMapping("/release-note")
    public ResponseEntity registerReleaseNote(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @Validated @RequestBody RegisterReleaseNoteRequest request){
        releaseNoteService.registerReleaseNote(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("릴리즈 노트 등록에 성공했습니다"), CREATED);
    }
}
