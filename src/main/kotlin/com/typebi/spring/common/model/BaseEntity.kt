package com.typebi.spring.common.model

import jakarta.persistence.Column
import java.time.LocalDateTime

open class BaseEntity {
    @Column(name = "created_at")
    val createdAt : LocalDateTime = LocalDateTime.now()
    @Column(name = "updated_at")
    var updatedAt : LocalDateTime = LocalDateTime.now()
}