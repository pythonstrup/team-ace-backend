package com.nexters.teamace.common.infrastructure;

import com.nexters.teamace.common.application.DateTimeHolder;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
class LocalDateTimeHolder implements DateTimeHolder {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
