package com.nexters.teamace.common.presentation;

import com.nexters.teamace.common.exception.ErrorCode;

public record ErrorMessage(ErrorCode code, String message, Object data) {}
