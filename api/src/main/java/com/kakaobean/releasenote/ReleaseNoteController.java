package com.kakaobean.releasenote;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.releasenote.application.ReleaseNoteService;
import com.kakaobean.core.releasenote.domain.repository.query.FindReleaseNoteResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindPagingReleaseNotesResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindReleaseNotesResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.ReleaseNoteQueryRepository;
import com.kakaobean.core.releasenote.exception.NotExistsReleaseNoteException;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.security.UserPrincipal;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Timed("api.releaseNote")
@RestController
@RequiredArgsConstructor
public class ReleaseNoteController {

    private final ReleaseNoteService releaseNoteService;
    private final ReleaseNoteQueryRepository releaseNoteQueryRepository;


    @PostMapping("/release-notes")
    public ResponseEntity deployReleaseNote(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @Validated @RequestBody DeployReleaseNoteRequest request) {
        log.info("릴리즈 노트 배포 api 시작");
        Long releaseNoteId = releaseNoteService.deployReleaseNote(request.toServiceDto(userPrincipal.getId()));
        log.info("릴리즈 노트 배포 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from(releaseNoteId, "릴리즈 노트 등록에 성공했습니다"), CREATED);
    }

    @GetMapping("/release-notes/page")
    public ResponseEntity findReleaseNotes(@RequestParam Long projectId, @RequestParam Integer page) {
        log.info("릴리즈 노트 페이징 조회 api 시작");
        FindPagingReleaseNotesResponseDto response = releaseNoteQueryRepository.findByProjectId(projectId, page);
        log.info("릴리즈 노트 페이징 조회 api 종료");
        return new ResponseEntity(response, OK);
    }

    @GetMapping("/release-notes")
    public ResponseEntity findReleaseNotes(@RequestParam Long projectId) {
        log.info("릴리즈 노트 전체 조회 api 시작");
        FindReleaseNotesResponseDto response = releaseNoteQueryRepository.findAllByProjectId(projectId);
        log.info("릴리즈 노트 전체 조회 api 종료");
        return new ResponseEntity(response, OK);
    }

    @GetMapping("/release-notes/{releaseNoteId}")
    public ResponseEntity findReleaseNote(@PathVariable Long releaseNoteId) {
        log.info("릴리즈 노트 단건 조회 api 시작");
        FindReleaseNoteResponseDto response = releaseNoteQueryRepository.findById(releaseNoteId)
                .orElseThrow(NotExistsReleaseNoteException::new);
        log.info("릴리즈 노트 단건 조회 api 종료");
        return new ResponseEntity(response, OK);
    }
}
