package com.nexters.teamace.common.infrastructure;

import com.nexters.teamace.common.application.SystemHolder;
import org.springframework.stereotype.Component;

@Component
class JavaSystemHolder implements SystemHolder {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
