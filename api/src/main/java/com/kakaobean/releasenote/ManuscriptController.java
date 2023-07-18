package com.kakaobean.releasenote;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptsResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.ManuscriptQueryRepository;
import com.kakaobean.core.releasenote.exception.NotExistsManuscriptException;
import com.kakaobean.releasenote.dto.request.RegisterManuscriptRequest;
import com.kakaobean.security.UserPrincipal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ManuscriptController {

    private final ManuscriptService manuscriptService;
    private final ManuscriptQueryRepository manuscriptQueryRepository;

    @PostMapping("/manuscripts")
    public ResponseEntity registerManuscript(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @RequestBody @Validated RegisterManuscriptRequest request){
        manuscriptService.registerManuscript(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("릴리즈 노트 원고 등록에 성공했습니다."), HttpStatus.CREATED);
    }

    @GetMapping("/manuscripts/{manuscriptId}")
    public ResponseEntity findManuscript(@PathVariable Long manuscriptId){
        FindManuscriptResponseDto response = manuscriptQueryRepository.findById(manuscriptId).orElseThrow(NotExistsManuscriptException::new);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/manuscripts")
    public ResponseEntity findManuscripts(@RequestParam Long projectId, @RequestParam Integer page){
        FindManuscriptsResponseDto response = manuscriptQueryRepository.findByProjectId(projectId, page);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
