package com.typebi.spring.db.repository

import com.typebi.spring.db.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {
    fun findByPostId(id: Long): List<Comment>
}