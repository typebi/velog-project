package com.typebi.spring.comment.repository

import com.typebi.spring.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {
    fun findByPostId(id: Long): List<Comment>
}