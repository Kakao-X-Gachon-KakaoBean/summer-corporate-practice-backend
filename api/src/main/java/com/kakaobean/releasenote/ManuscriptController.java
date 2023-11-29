package com.kakaobean.releasenote;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.application.dto.response.ManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptsResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindPagingManuscriptsResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.ManuscriptQueryRepository;
import com.kakaobean.core.releasenote.exception.NotExistsManuscriptException;
import com.kakaobean.releasenote.dto.request.ModifyManuscriptRequest;
import com.kakaobean.releasenote.dto.request.RegisterManuscriptRequest;
import com.kakaobean.security.UserPrincipal;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Timed("api.manuscript")
@RestController
@RequiredArgsConstructor
public class ManuscriptController {

    private final ManuscriptService manuscriptService;
    private final ManuscriptQueryRepository manuscriptQueryRepository;

    @PostMapping("/manuscripts")
    public ResponseEntity registerManuscript(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @RequestBody @Validated RegisterManuscriptRequest request) {
        log.info("릴리즈 노트 원고 등록 api 시작");
        Long manuscriptId = manuscriptService.registerManuscript(request.toServiceDto(userPrincipal.getId()));
        log.info("릴리즈 노트 원고 등록 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from(manuscriptId, "릴리즈 노트 원고 등록에 성공했습니다."), HttpStatus.CREATED);
    }

    @GetMapping("/manuscripts/{manuscriptId}")
    public ResponseEntity findManuscript(@PathVariable Long manuscriptId) {
        log.info("릴리즈 노트 원고 조회 api 시작");
        FindManuscriptResponseDto response = manuscriptQueryRepository.findById(manuscriptId).orElseThrow(NotExistsManuscriptException::new);
        log.info("릴리즈 노트 원고 조회 api 종료");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/manuscripts/page")
    public ResponseEntity findManuscripts(@RequestParam Long projectId, @RequestParam Integer page) {
        log.info("릴리즈 노트 원고 페이징 조회 api 시작");
        FindPagingManuscriptsResponseDto response = manuscriptQueryRepository.findByProjectIdWithPaging(projectId, page);
        log.info("릴리즈 노트 원고 페이징 조회 api 종료");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/manuscripts")
    public ResponseEntity findManuscripts(@RequestParam Long projectId){
        log.info("릴리즈 노트 원고 전체 조회 api 시작");
        FindManuscriptsResponseDto response = manuscriptQueryRepository.findAllByProjectId(projectId);
        log.info("릴리즈 노트 원고 전체 조회 api 종료");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping("/manuscripts/{manuscriptId}/access-status")
    public ResponseEntity hasRightToModifyManuscript(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @PathVariable Long manuscriptId) {
        log.info("릴리즈 노트 원고 수정 권한 획득 api 시작");
        ManuscriptResponseDto response = manuscriptService.hasRightToModifyManuscript(userPrincipal.getId(), manuscriptId);
        log.info("릴리즈 노트 원고 수정 권한 획득 api 종료");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping("/manuscripts/{manuscriptId}")
    public ResponseEntity modifyManuscript(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @RequestBody @Validated ModifyManuscriptRequest request,
                                           @PathVariable Long manuscriptId) {
        log.info("릴리즈 노트 원고 수정 api 시작");
        manuscriptService.modifyManuscript(request.toServiceDto(userPrincipal.getId(), manuscriptId));
        log.info("릴리즈 노트 원고 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("릴리즈 노트 원고 수정에 성공했습니다."), HttpStatus.OK);
    }

    @DeleteMapping("/manuscripts/{manuscriptId}")
    public ResponseEntity deleteManuscript(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @PathVariable Long manuscriptId) {
        log.info("릴리즈 노트 원고 삭제 api 시작");
        manuscriptService.deleteManuscript(userPrincipal.getId(), manuscriptId);
        log.info("릴리즈 노트 원고 삭제 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("릴리즈 노트 원고 삭제에 성공했습니다."), HttpStatus.OK);
    }
}
