package com.nexters.teamace.fairy.application;

import com.nexters.teamace.fairy.domain.AcquiredFairy;
import com.nexters.teamace.fairy.domain.AcquiredFairyRepository;
import com.nexters.teamace.fairy.domain.Fairy;
import com.nexters.teamace.fairy.domain.FairyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcquiredFairyService {

    private final FairyRepository fairyRepository;
    private final AcquiredFairyRepository acquiredFairyRepository;

    public AcquiredFairyResult save(final AcquiredFairyCommand command) {
        final Fairy fairy = fairyRepository.getById(command.fairyId());
        final AcquiredFairy acquiredFairy =
                acquiredFairyRepository.save(
                        new AcquiredFairy(command.fairyId(), command.userId()));
        return new AcquiredFairyResult(fairy, acquiredFairy);
    }
}
