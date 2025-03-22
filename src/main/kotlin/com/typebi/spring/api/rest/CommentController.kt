package com.typebi.spring.api.rest

import com.typebi.spring.api.requests.CommentCreateDTO
import com.typebi.spring.api.requests.CommentUpdateDTO
import com.typebi.spring.api.responses.CommentResponseDTO
import com.typebi.spring.api.service.CommentService
import com.typebi.spring.common.exception.BadRequestException
import com.typebi.spring.common.response.ApiResponse
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping
    fun createComment(@RequestBody commentCreateDTO: CommentCreateDTO): ResponseEntity<ApiResponse<CommentResponseDTO>> {
        val commentResponseDTO = commentService.createComment(commentCreateDTO)
        val location = URI.create("/api/v1/comments/${commentResponseDTO.id}")
        val response = ApiResponse(true, commentResponseDTO, "Comment created successfully", HttpStatus.CREATED.value())
        return ResponseEntity.created(location).body(response)
    }
    @GetMapping
    fun getComments(): ResponseEntity<ApiResponse<List<CommentResponseDTO>>> {
        val commentDTOs = commentService.getComments()
        val response = ApiResponse(true, commentDTOs, "Comments retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{commentId:\\d+}")
    fun getCommentById(@PathVariable(name = "commentId") commentId: Long): ResponseEntity<ApiResponse<CommentResponseDTO>> {
        val commentDTO = commentService.getCommentById(commentId)
        commentDTO.add(
            linkTo<CommentController> { getCommentById(commentId) }.withSelfRel(),
            linkTo<CommentController> { updateCommentById(commentId, null) }.withRel("update"),
            linkTo<CommentController> { deleteCommentById(commentId) }.withRel("delete"),
            linkTo<UserController> { getUserById(commentDTO.authorId) }.withRel("author")
        )
        val response = ApiResponse(true, commentDTO, "Comment with ID $commentId retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{commentId:\\d+}")
    fun updateCommentById(
        @PathVariable(name = "commentId") commentId: Long,
        @RequestBody commentUpdateDTO: CommentUpdateDTO?
    ): ResponseEntity<ApiResponse<CommentResponseDTO>> {
        if (commentUpdateDTO == null) {
            throw BadRequestException("Request body is null")
        }
        val commentDTO = commentService.updateCommentById(commentId, commentUpdateDTO)
        val response = ApiResponse(true, commentDTO, "Comment with ID $commentId updated successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{commentId:\\d+}")
    fun deleteCommentById(@PathVariable(name = "commentId") commentId: Long): ResponseEntity<ApiResponse<Unit>> {
        val result = commentService.deleteCommentById(commentId)
        val response = ApiResponse(result, Unit, "Comment with ID $commentId deleted successfully", HttpStatus.NO_CONTENT.value())
        return ResponseEntity.ok(response)
    }
}