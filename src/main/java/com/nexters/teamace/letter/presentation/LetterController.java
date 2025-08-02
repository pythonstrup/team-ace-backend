package com.nexters.teamace.letter.presentation;

import com.nexters.teamace.common.presentation.ApiResponse;
import com.nexters.teamace.common.presentation.AuthUser;
import com.nexters.teamace.common.presentation.UserInfo;
import com.nexters.teamace.letter.application.CreateLetterCommand;
import com.nexters.teamace.letter.application.CreateLetterResult;
import com.nexters.teamace.letter.application.LetterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/letters")
public class LetterController {

    private final LetterService letterService;

    @PostMapping
    public ApiResponse<CreateLetterResponse> createLetter(
            @AuthUser final UserInfo user, @RequestBody @Valid final CreateLetterRequest request) {
        final CreateLetterCommand command =
                new CreateLetterCommand(
                        user.userId(), request.chatRoomId(), request.fairyId(), request.contents());
        final CreateLetterResult result = letterService.createLetter(command);
        return ApiResponse.success(
                new CreateLetterResponse(
                        result.fairyId(), result.name(), result.image(), result.contents()));
    }
}
