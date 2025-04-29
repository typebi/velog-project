package com.typebi.spring.common.response

data class CursorPage<T>(
    val content: List<T>,
    val hasNext: Boolean,
    val nextCursor: Long?
)