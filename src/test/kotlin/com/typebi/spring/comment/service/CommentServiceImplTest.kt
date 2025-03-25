package com.typebi.spring.comment.service

import com.typebi.spring.comment.requests.CommentCreateDTO
import com.typebi.spring.comment.requests.CommentUpdateDTO
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.entity.Comment
import com.typebi.spring.db.entity.Post
import com.typebi.spring.db.entity.User
import com.typebi.spring.db.repository.CommentRepository
import com.typebi.spring.db.repository.PostRepository
import com.typebi.spring.db.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.cache.CacheManager
import java.util.*

class CommentServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var postRepository: PostRepository
    private lateinit var commentRepository: CommentRepository
    private lateinit var cacheManager: CacheManager
    private lateinit var commentService: CommentService

    private lateinit var mockUser1: User
    private lateinit var mockUser2: User
    private lateinit var mockPost1: Post
    private lateinit var mockPost2: Post
    private lateinit var mockComment1: Comment
    private lateinit var mockComment2: Comment

    @BeforeEach
    fun setUp() {
        userRepository = Mockito.mock(UserRepository::class.java)
        postRepository = Mockito.mock(PostRepository::class.java)
        commentRepository = Mockito.mock(CommentRepository::class.java)
        commentService = CommentServiceImpl(userRepository, postRepository, commentRepository, cacheManager)

        mockUser1 = Mockito.mock(User::class.java)
        mockUser2 = Mockito.mock(User::class.java)
        mockPost1 = Mockito.mock(Post::class.java)
        mockPost2 = Mockito.mock(Post::class.java)
        mockComment1 = Mockito.mock(Comment::class.java)
        mockComment2 = Mockito.mock(Comment::class.java)
        Mockito.`when`(mockUser1.id).thenReturn(1L)
        Mockito.`when`(mockUser2.id).thenReturn(2L)
        Mockito.`when`(mockPost1.id).thenReturn(1L)
        Mockito.`when`(mockPost2.id).thenReturn(2L)
        Mockito.`when`(mockComment1.id).thenReturn(1L)
        Mockito.`when`(mockComment1.post).thenReturn(mockPost1)
        Mockito.`when`(mockComment1.content).thenReturn("mockComment1.content")
        Mockito.`when`(mockComment1.author).thenReturn(mockUser1)
        Mockito.`when`(mockComment2.id).thenReturn(2L)
        Mockito.`when`(mockComment2.post).thenReturn(mockPost1)
        Mockito.`when`(mockComment2.author).thenReturn(mockUser1)
        Mockito.`when`(mockComment2.content).thenReturn("mockComment2.content")

        Mockito.`when`(postRepository.findById(mockPost1.id)).thenReturn(Optional.of(mockPost1))
        Mockito.`when`(postRepository.findById(mockPost2.id)).thenReturn(Optional.of(mockPost2))
        Mockito.`when`(userRepository.findById(mockUser1.id)).thenReturn(Optional.of(mockUser1))
        Mockito.`when`(userRepository.findById(mockUser2.id)).thenReturn(Optional.of(mockUser2))
        Mockito.`when`(commentRepository.findById(mockComment1.id)).thenReturn(Optional.of(mockComment1))
        Mockito.`when`(commentRepository.findById(mockComment2.id)).thenReturn(Optional.of(mockComment2))
    }

    @Test
    fun createCommentTest() {
        Mockito.`when`(commentRepository.save(any())).thenReturn(mockComment1)

        val commentCreateDTO = CommentCreateDTO(mockPost1.id, "mockComment1.content", mockUser1.id)
        val result = commentService.createComment(commentCreateDTO)

        Assertions.assertEquals(mockPost1.id, result.postId)
        Assertions.assertEquals(commentCreateDTO.content, result.content)
        Assertions.assertEquals(mockUser1.id, result.authorId)

        Mockito.verify(userRepository, Mockito.times(1)).findById(mockUser1.id)
        Mockito.verify(postRepository, Mockito.times(1)).findById(mockPost1.id)
        Mockito.verify(commentRepository, Mockito.times(1)).save(any())
    }

    @Test
    fun createPostNotFoundTest() {
        Mockito.`when`(userRepository.findById(0L)).thenReturn(Optional.empty())
        Mockito.`when`(postRepository.findById(0L)).thenReturn(Optional.empty())

        val commentCreateDTO = CommentCreateDTO(0L, "mockComment1.content", mockUser1.id)
        org.junit.jupiter.api.assertThrows<NotFoundException> { commentService.createComment(commentCreateDTO) }

        Mockito.verify(postRepository, Mockito.times(1)).findById(0)
        Mockito.verify(commentRepository, Mockito.times(0)).save(any())
    }

    @Test
    fun getCommentsTest() {
        val comments = listOf(mockComment1, mockComment2)

        Mockito.`when`(commentRepository.findAll()).thenReturn(comments)

        val result = commentService.getComments()

        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(comments[0].id, result[0].id)
        Assertions.assertEquals(comments[0].post.id, result[0].postId)
        Assertions.assertEquals(comments[0].content, result[0].content)
        Assertions.assertEquals(comments[0].author.id, result[0].authorId)
        Assertions.assertEquals(comments[1].id, result[1].id)
        Assertions.assertEquals(comments[1].post.id, result[0].postId)
        Assertions.assertEquals(comments[1].content, result[1].content)
        Assertions.assertEquals(comments[1].author.id, result[1].authorId)

        Mockito.verify(commentRepository, Mockito.times(1)).findAll()
    }

    @Test
    fun getCommentByIdTest() {
        val result = commentService.getCommentById(mockComment1.id)

        Assertions.assertEquals(mockComment1.id, result.id)
        Assertions.assertEquals(mockComment1.post.id, result.postId)
        Assertions.assertEquals(mockComment1.content, result.content)
        Assertions.assertEquals(mockComment1.author.id, result.authorId)

        Mockito.verify(commentRepository, Mockito.times(1)).findById(mockComment1.id)
    }

    @Test
    fun getCommentByIdNotFoundTest() {
        Mockito.`when`(commentRepository.findById(0)).thenReturn(Optional.empty())

        org.junit.jupiter.api.assertThrows<NotFoundException> { commentService.getCommentById(0) }

        Mockito.verify(commentRepository, Mockito.times(1)).findById(0)
    }

    @Test
    fun updateCommentByIdTest() {
        val commentUpdateDTO = CommentUpdateDTO(mockPost1.id, "updateTest", mockUser1.id)

        val result = commentService.updateCommentById(mockComment1.id, commentUpdateDTO)

        Assertions.assertEquals(mockComment1.id, result.id)
        Assertions.assertEquals(mockComment1.post.id, result.postId)
        Assertions.assertEquals(mockComment1.content, result.content)
        Assertions.assertEquals(mockComment1.author.id, result.authorId)

        Mockito.verify(postRepository, Mockito.times(1)).findById(mockPost1.id)
        Mockito.verify(userRepository, Mockito.times(1)).findById(mockUser1.id)
        Mockito.verify(commentRepository, Mockito.times(1)).findById(mockComment1.id)
    }

    @Test
    fun updateCommentByIdNotFoundTest() {
        Mockito.`when`(commentRepository.findById(0)).thenReturn(Optional.empty())

        val commentUpdateDTO = CommentUpdateDTO(mockPost1.id, "updateTest", mockUser1.id)

        org.junit.jupiter.api.assertThrows<NotFoundException> { commentService.updateCommentById(0, commentUpdateDTO) }

        Mockito.verify(commentRepository, Mockito.times(1)).findById(0)
    }

    @Test
    fun deleteCommentByIdTest() {
        Mockito.doNothing().`when`(commentRepository).delete(mockComment1)

        val result = commentService.deleteCommentById(mockComment1.id)

        Assertions.assertTrue(result)

        Mockito.verify(commentRepository, Mockito.times(1)).findById(mockComment1.id)
        Mockito.verify(commentRepository, Mockito.times(1)).delete(mockComment1)
    }

    @Test
    fun deleteCommentByIdNotFoundTest() {
        Mockito.`when`(commentRepository.findById(0)).thenReturn(Optional.empty())

        org.junit.jupiter.api.assertThrows<NotFoundException> { commentService.deleteCommentById(0) }

        Mockito.verify(commentRepository, Mockito.times(1)).findById(0)
    }
}