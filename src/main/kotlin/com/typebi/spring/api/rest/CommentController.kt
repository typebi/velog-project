package com.typebi.spring.api.rest

import com.typebi.spring.api.requests.CommentCreateDTO
import com.typebi.spring.api.requests.CommentUpdateDTO
import com.typebi.spring.api.responses.CommentResponseDTO
import com.typebi.spring.api.service.CommentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping
    fun createComment(@RequestBody commentCreateDTO: CommentCreateDTO): CommentResponseDTO {
        return commentService.createComment(commentCreateDTO)
    }
    @GetMapping
    fun getComments(): List<CommentResponseDTO> {
        return commentService.getComments()
    }

    @GetMapping("/{commentId:\\d+}")
    fun getCommentById(@PathVariable(name = "commentId") commentId: Long): CommentResponseDTO {
        return commentService.getCommentById(commentId)
    }

    @PatchMapping("/{commentId:\\d+}")
    fun updateCommentById(
        @PathVariable(name = "commentId") commentId: Long,
        @RequestBody commentUpdateDTO: CommentUpdateDTO
    ): CommentResponseDTO {
        return commentService.updateCommentById(commentId, commentUpdateDTO)
    }

    @DeleteMapping("/{commentId:\\d+}")
    fun deleteCommentById(@PathVariable(name = "commentId") commentId: Long) {
        commentService.deleteCommentById(commentId)
    }
}