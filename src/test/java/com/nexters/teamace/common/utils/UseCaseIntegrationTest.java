package com.nexters.teamace.common.utils;

import com.nexters.teamace.common.application.DateTimeHolder;
import com.nexters.teamace.common.application.SystemHolder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class UseCaseIntegrationTest {

    @MockitoBean protected SystemHolder systemHolder;
    @MockitoBean protected DateTimeHolder dateTimeHolder;
}
