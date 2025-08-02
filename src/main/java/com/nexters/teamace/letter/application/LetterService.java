package com.nexters.teamace.letter.application;

import com.nexters.teamace.fairy.application.AcquiredFairyCommand;
import com.nexters.teamace.fairy.application.AcquiredFairyResult;
import com.nexters.teamace.fairy.application.AcquiredFairyService;
import com.nexters.teamace.letter.domain.Letter;
import com.nexters.teamace.letter.domain.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {

    private final LetterRepository letterRepository;
    private final AcquiredFairyService acquiredFairyService;

    @Transactional
    public CreateLetterResult createLetter(CreateLetterCommand command) {

        final Letter letter =
                Letter.create(command.chatRoomId(), command.fairyId(), command.contents());
        final Letter savedLetter = letterRepository.save(letter);

        final AcquiredFairyCommand fairyCommand =
                new AcquiredFairyCommand(command.fairyId(), command.userId());
        final AcquiredFairyResult fairy = acquiredFairyService.save(fairyCommand);

        return new CreateLetterResult(
                fairy.fairy().getId(),
                fairy.fairy().getName(),
                fairy.fairy().getImageUrl(),
                savedLetter.getContents());
    }
}
