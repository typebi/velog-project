package com.typebi.spring.utils

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Aspect
@Component
// @Profile("dev") 추후 적용
class QueryCounterAspect {
    var queryCount = 0

    fun resetCount() {
        queryCount = 0
    }

    @Before("execution(* com.typebi.spring.db.repository.PostRepository.findById(..))")
    fun countFindByIdCalls() {
        queryCount++
    }
}