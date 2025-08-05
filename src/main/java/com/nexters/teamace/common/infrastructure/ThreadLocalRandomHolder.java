package com.nexters.teamace.common.infrastructure;

import com.nexters.teamace.common.application.RandomHolder;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
class ThreadLocalRandomHolder implements RandomHolder {

    @Override
    public int nextInt(final int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}
