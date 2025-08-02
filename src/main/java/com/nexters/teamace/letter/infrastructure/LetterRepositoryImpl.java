package com.nexters.teamace.letter.infrastructure;

import com.nexters.teamace.letter.domain.Letter;
import com.nexters.teamace.letter.domain.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterRepositoryImpl implements LetterRepository {

    private final LetterJpaRepository letterJpaRepository;
    private final LetterMapper letterMapper;

    @Override
    public Letter save(final Letter letter) {
        final LetterEntity entity = letterMapper.toEntity(letter);
        final LetterEntity savedEntity = letterJpaRepository.save(entity);
        return letterMapper.toDomain(savedEntity);
    }
}
