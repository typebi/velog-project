package com.typebi.spring.api.service

import com.typebi.spring.api.requests.CommentCreateDTO
import com.typebi.spring.api.requests.CommentUpdateDTO
import com.typebi.spring.api.responses.CommentResponseDTO
import org.springframework.stereotype.Service

@Service
interface CommentService {
    fun createComment(commentCreateDTO: CommentCreateDTO): CommentResponseDTO
    fun getComments(): List<CommentResponseDTO>
    fun getCommentById(id: Long): CommentResponseDTO
    fun updateCommentById(id: Long, commentUpdateDTO: CommentUpdateDTO): CommentResponseDTO
    fun deleteCommentById(id: Long): Boolean

}
