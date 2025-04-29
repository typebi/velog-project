package com.typebi.spring.post.repository

import com.typebi.spring.post.model.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository: JpaRepository<Post, Long> {
    fun findByAuthorId(id: Long): List<Post>
    @Query("SELECT * FROM posts p ORDER BY p.id DESC LIMIT :n", nativeQuery = true)
    fun findTopNById(@Param("n") n: Long): List<Post>
    fun findNextPageByIdLessThanOrderByIdDesc(nextCursor: Long, pageable: Pageable): List<Post>
}