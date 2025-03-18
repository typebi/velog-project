package com.typebi.spring.api.service

import com.typebi.spring.api.requests.CommentCreateDTO
import com.typebi.spring.api.requests.CommentUpdateDTO
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.entity.Comment
import com.typebi.spring.db.entity.Post
import com.typebi.spring.db.entity.User
import com.typebi.spring.db.repository.CommentRepository
import com.typebi.spring.db.repository.PostRepository
import com.typebi.spring.db.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import java.util.*

class CommentServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var postRepository: PostRepository
    private lateinit var commentRepository: CommentRepository
    private lateinit var commentService: CommentService

    private lateinit var mockUser1: User
    private lateinit var mockUser2: User
    private lateinit var mockPost1: Post
    private lateinit var mockPost2: Post
    private lateinit var mockComment1: Comment
    private lateinit var mockComment2: Comment

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        postRepository = mock(PostRepository::class.java)
        commentRepository = mock(CommentRepository::class.java)
        commentService = CommentServiceImpl(userRepository, postRepository, commentRepository)

        mockUser1 = mock(User::class.java)
        mockUser2 = mock(User::class.java)
        mockPost1 = mock(Post::class.java)
        mockPost2 = mock(Post::class.java)
        mockComment1 = mock(Comment::class.java)
        mockComment2 = mock(Comment::class.java)
        `when`(mockUser1.id).thenReturn(1L)
        `when`(mockUser2.id).thenReturn(2L)
        `when`(mockPost1.id).thenReturn(1L)
        `when`(mockPost2.id).thenReturn(2L)
        `when`(mockComment1.id).thenReturn(1L)
        `when`(mockComment1.post).thenReturn(mockPost1)
        `when`(mockComment1.content).thenReturn("mockComment1.content")
        `when`(mockComment1.author).thenReturn(mockUser1)
        `when`(mockComment2.id).thenReturn(2L)
        `when`(mockComment2.post).thenReturn(mockPost1)
        `when`(mockComment2.author).thenReturn(mockUser1)
        `when`(mockComment2.content).thenReturn("mockComment2.content")

        `when`(postRepository.findById(mockPost1.id)).thenReturn(Optional.of(mockPost1))
        `when`(postRepository.findById(mockPost2.id)).thenReturn(Optional.of(mockPost2))
        `when`(userRepository.findById(mockUser1.id)).thenReturn(Optional.of(mockUser1))
        `when`(userRepository.findById(mockUser2.id)).thenReturn(Optional.of(mockUser2))
        `when`(commentRepository.findById(mockComment1.id)).thenReturn(Optional.of(mockComment1))
        `when`(commentRepository.findById(mockComment2.id)).thenReturn(Optional.of(mockComment2))
    }

    @Test
    fun createCommentTest() {
        `when`(commentRepository.save(any())).thenReturn(mockComment1)

        val commentCreateDTO = CommentCreateDTO(mockPost1.id, "mockComment1.content", mockUser1.id)
        val result = commentService.createComment(commentCreateDTO)

        assertEquals(mockPost1.id, result.postId)
        assertEquals(commentCreateDTO.content, result.content)
        assertEquals(mockUser1.id, result.authorId)

        verify(userRepository, times(1)).findById(mockUser1.id)
        verify(postRepository, times(1)).findById(mockPost1.id)
        verify(commentRepository, times(1)).save(any())
    }

    @Test
    fun createPostNotFoundTest() {
        `when`(userRepository.findById(0L)).thenReturn(Optional.empty())
        `when`(postRepository.findById(0L)).thenReturn(Optional.empty())

        val commentCreateDTO = CommentCreateDTO(0L, "mockComment1.content", mockUser1.id)
        org.junit.jupiter.api.assertThrows<NotFoundException> { commentService.createComment(commentCreateDTO) }

        verify(postRepository, times(1)).findById(0)
        verify(commentRepository, times(0)).save(any())
    }

    @Test
    fun getCommentsTest() {
        val comments = listOf(mockComment1, mockComment2)

        `when`(commentRepository.findAll()).thenReturn(comments)

        val result = commentService.getComments()

        assertEquals(2, result.size)
        assertEquals(comments[0].id, result[0].id)
        assertEquals(comments[0].post.id, result[0].postId)
        assertEquals(comments[0].content, result[0].content)
        assertEquals(comments[0].author.id, result[0].authorId)
        assertEquals(comments[1].id, result[1].id)
        assertEquals(comments[1].post.id, result[0].postId)
        assertEquals(comments[1].content, result[1].content)
        assertEquals(comments[1].author.id, result[1].authorId)

        verify(commentRepository, times(1)).findAll()
    }

    @Test
    fun getCommentByIdTest() {
        val result = commentService.getCommentById(mockComment1.id)

        assertEquals(mockComment1.id, result.id)
        assertEquals(mockComment1.post.id, result.postId)
        assertEquals(mockComment1.content, result.content)
        assertEquals(mockComment1.author.id, result.authorId)

        verify(commentRepository, times(1)).findById(mockComment1.id)
    }

    @Test
    fun getCommentByIdNotFoundTest() {
        `when`(commentRepository.findById(0)).thenReturn(Optional.empty())

        org.junit.jupiter.api.assertThrows<NotFoundException> { commentService.getCommentById(0) }

        verify(commentRepository, times(1)).findById(0)
    }

    @Test
    fun updateCommentByIdTest() {
        val commentUpdateDTO = CommentUpdateDTO(mockPost1.id, "updateTest", mockUser1.id)

        val result = commentService.updateCommentById(mockComment1.id, commentUpdateDTO)

        assertEquals(mockComment1.id, result.id)
        assertEquals(mockComment1.post.id, result.postId)
        assertEquals(mockComment1.content, result.content)
        assertEquals(mockComment1.author.id, result.authorId)

        verify(postRepository, times(1)).findById(mockPost1.id)
        verify(userRepository, times(1)).findById(mockUser1.id)
        verify(commentRepository,times(1)).findById(mockComment1.id)
    }

    @Test
    fun updateCommentByIdNotFoundTest() {
        `when`(commentRepository.findById(0)).thenReturn(Optional.empty())

        val commentUpdateDTO = CommentUpdateDTO(mockPost1.id, "updateTest", mockUser1.id)

        org.junit.jupiter.api.assertThrows<NotFoundException> { commentService.updateCommentById(0, commentUpdateDTO) }

        verify(commentRepository, times(1)).findById(0)
    }

    @Test
    fun deleteCommentByIdTest() {
        doNothing().`when`(commentRepository).delete(mockComment1)

        val result = commentService.deleteCommentById(mockComment1.id)

        assertTrue(result)

        verify(commentRepository, times(1)).findById(mockComment1.id)
        verify(commentRepository, times(1)).delete(mockComment1)
    }

    @Test
    fun deleteCommentByIdNotFoundTest() {
        `when`(commentRepository.findById(0)).thenReturn(Optional.empty())

        org.junit.jupiter.api.assertThrows<NotFoundException> { commentService.deleteCommentById(0) }

        verify(commentRepository, times(1)).findById(0)
    }
}