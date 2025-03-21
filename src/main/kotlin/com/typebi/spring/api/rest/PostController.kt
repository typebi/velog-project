package com.typebi.spring.api.rest

import com.typebi.spring.api.requests.PostCreateDTO
import com.typebi.spring.api.requests.PostUpdateDTO
import com.typebi.spring.api.responses.CommentResponseDTO
import com.typebi.spring.api.responses.PostResponseDTO
import com.typebi.spring.api.service.PostService
import com.typebi.spring.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService
) {
    @PostMapping
    fun createPost(@RequestBody postCreateDTO: PostCreateDTO): ResponseEntity<ApiResponse<PostResponseDTO>> {
        val postDTO = postService.createPost(postCreateDTO)
        val location = URI.create("/api/v1/posts/${postDTO.id}")
        val response = ApiResponse(true, postDTO, "Post created successfully", HttpStatus.CREATED.value())
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    fun getPosts(): ResponseEntity<ApiResponse<List<PostResponseDTO>>> {
        val postDTOs = postService.getPosts()
        val response = ApiResponse(true, postDTOs, "Posts retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{postId:\\d+}")
    fun getPostById(@PathVariable(name = "postId") postId: Long): ResponseEntity<ApiResponse<PostResponseDTO>> {
        val postDTO = postService.getPostById(postId)
        val response = ApiResponse(true, postDTO, "Post with ID $postId retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{postId:\\d+}/comments")
    fun getCommentsByPostId(@PathVariable(name = "postId") postId: Long): ResponseEntity<ApiResponse<List<CommentResponseDTO>>> {
        val postDTO = postService.getCommentsByPostId(postId)
        val response = ApiResponse(true, postDTO, "Comments with post ID $postId retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{postId:\\d+}")
    fun updatePostById(
        @PathVariable(name = "postId") postId: Long,
        @RequestBody postUpdateDTO: PostUpdateDTO
    ): ResponseEntity<ApiResponse<PostResponseDTO>> {
        val postDTO = postService.updatePostById(postId, postUpdateDTO)
        val response = ApiResponse(true, postDTO, "User with ID $postId updated successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{postId:\\d+}")
    fun deletePostById(@PathVariable(name = "postId") postId: Long): ResponseEntity<ApiResponse<Unit>> {
        val result = postService.deletePostById(postId)
        val response = ApiResponse(result, Unit, "Post with ID $postId deleted successfully", HttpStatus.NO_CONTENT.value())
        return ResponseEntity.ok(response)
    }
}