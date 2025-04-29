package com.typebi.spring.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(msg: String): RuntimeException(msg)
