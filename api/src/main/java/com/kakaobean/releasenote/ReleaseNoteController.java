package com.kakaobean.releasenote;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.releasenote.application.ReleaseNoteService;
import com.kakaobean.core.releasenote.domain.repository.query.FindReleaseNoteResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindReleaseNotesResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.ReleaseNoteQueryRepository;
import com.kakaobean.core.releasenote.exception.NotExistsReleaseNoteException;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequiredArgsConstructor
public class ReleaseNoteController {

    private final ReleaseNoteService releaseNoteService;
    private final ReleaseNoteQueryRepository releaseNoteQueryRepository;


    @PostMapping("/release-notes")
    public ResponseEntity deployReleaseNote(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @Validated @RequestBody DeployReleaseNoteRequest request) {
        releaseNoteService.deployReleaseNote(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("릴리즈 노트 등록에 성공했습니다"), CREATED);
    }

    @GetMapping("/release-notes")
    public ResponseEntity findReleaseNotes(@RequestParam Long projectId, @RequestParam Integer page) {
        FindReleaseNotesResponseDto response = releaseNoteQueryRepository.findByProjectId(projectId, page);
        return new ResponseEntity(response, OK);
    }

    @GetMapping("/release-notes/{releaseNoteId}")
    public ResponseEntity findReleaseNotes(@PathVariable Long releaseNoteId) {
        FindReleaseNoteResponseDto response = releaseNoteQueryRepository.findById(releaseNoteId)
                .orElseThrow(NotExistsReleaseNoteException::new);
        return new ResponseEntity(response, OK);
    }
}
