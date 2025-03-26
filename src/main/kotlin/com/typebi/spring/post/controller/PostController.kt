package com.typebi.spring.post.controller

import com.typebi.spring.post.requests.PostCreateDTO
import com.typebi.spring.post.requests.PostUpdateDTO
import com.typebi.spring.comment.responses.CommentResponseDTO
import com.typebi.spring.post.responses.PostResponseDTO
import com.typebi.spring.user.controller.UserController
import com.typebi.spring.post.service.PostCommandService
import com.typebi.spring.post.service.PostQueryService
import com.typebi.spring.common.exception.BadRequestException
import com.typebi.spring.common.response.ApiResponse
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postCommandService: PostCommandService,
    private val postQueryService: PostQueryService
) {
    @PostMapping
    fun createPost(@RequestBody postCreateDTO: PostCreateDTO): ResponseEntity<ApiResponse<PostResponseDTO>> {
        val postDTO = postCommandService.createPost(postCreateDTO)
        val location = URI.create("/api/v1/posts/${postDTO.id}")
        val response = ApiResponse(true, postDTO, "Post created successfully", HttpStatus.CREATED.value())
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    fun getPosts(): ResponseEntity<ApiResponse<List<PostResponseDTO>>> {
        val postDTOs = postQueryService.getPosts()
        val response = ApiResponse(true, postDTOs, "Posts retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{postId:\\d+}")
    fun getPostById(@PathVariable(name = "postId") postId: Long): ResponseEntity<ApiResponse<PostResponseDTO>> {
        val postDTO = postQueryService.getPostById(postId)
        postDTO.add(
            linkTo<PostController> { getPostById(postId) }.withSelfRel(),
            linkTo<PostController> { updatePostById(postId, null) }.withRel("update"),
            linkTo<PostController> { deletePostById(postId) }.withRel("delete"),
            linkTo<PostController> { getCommentsByPostId(postId) }.withRel("comments"),
            linkTo<UserController> { getUserById(postDTO.authorId) }.withRel("author")
        )
        val response = ApiResponse(true, postDTO, "Post with ID $postId retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{postId:\\d+}/comments")
    fun getCommentsByPostId(@PathVariable(name = "postId") postId: Long): ResponseEntity<ApiResponse<List<CommentResponseDTO>>> {
        val commentDTOs = postQueryService.getCommentsByPostId(postId)
        val response = ApiResponse(
            true,
            commentDTOs,
            "Comments with post ID $postId retrieved successfully",
            HttpStatus.OK.value()
        )
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{postId:\\d+}")
    fun updatePostById(
        @PathVariable(name = "postId") postId: Long,
        @RequestBody postUpdateDTO: PostUpdateDTO?
    ): ResponseEntity<ApiResponse<PostResponseDTO>> {
        if (postUpdateDTO == null) {
            throw BadRequestException("Request body is null")
        }
        val postDTO = postCommandService.updatePostById(postId, postUpdateDTO)
        val response = ApiResponse(true, postDTO, "User with ID $postId updated successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{postId:\\d+}")
    fun deletePostById(@PathVariable(name = "postId") postId: Long): ResponseEntity<ApiResponse<Unit>> {
        val result = postCommandService.deletePostById(postId)
        val response =
            ApiResponse(result, Unit, "Post with ID $postId deleted successfully", HttpStatus.NO_CONTENT.value())
        return ResponseEntity.ok(response)
    }
}