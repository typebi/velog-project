package com.typebi.spring.api.rest

import com.typebi.spring.api.requests.PostCreateDTO
import com.typebi.spring.api.requests.PostUpdateDTO
import com.typebi.spring.api.responses.PostResponseDTO
import com.typebi.spring.api.service.PostService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService
) {
    @PostMapping
    fun createPost(@RequestBody postCreateDTO: PostCreateDTO): PostResponseDTO {
        return postService.createPost(postCreateDTO)
    }
    @GetMapping
    fun getPosts(): List<PostResponseDTO> {
        return postService.getPosts()
    }

    @GetMapping("/{postId:\\d+}")
    fun getPostById(@PathVariable(name = "postId") postId: Long): PostResponseDTO {
        return postService.getPostById(postId)
    }

    @PatchMapping("/{postId:\\d+}")
    fun updatePostById(
        @PathVariable(name = "postId") postId: Long,
        @RequestBody postUpdateDTO: PostUpdateDTO
    ): PostResponseDTO {
        return postService.updatePostById(postId, postUpdateDTO)
    }

    @DeleteMapping("/{postId:\\d+}")
    fun deletePostById(@PathVariable(name = "postId") postId: Long) {
        postService.deletePostById(postId)
    }
}