package com.typebi.spring.post.repository

import com.typebi.spring.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository: JpaRepository<Post, Long> {
    fun findByAuthorId(id: Long): List<Post>
}