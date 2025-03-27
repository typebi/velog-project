package com.typebi.spring.post.service

import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.comment.model.Comment
import com.typebi.spring.post.model.Post
import com.typebi.spring.user.model.User
import com.typebi.spring.comment.repository.CommentRepository
import com.typebi.spring.common.response.CursorPage
import com.typebi.spring.post.repository.PostRepository
import com.typebi.spring.user.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageImpl
import java.util.*

class PostQueryServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var postRepository: PostRepository
    private lateinit var commentRepository: CommentRepository
    private lateinit var postQueryService: PostQueryService

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
        postQueryService = PostQueryServiceImpl(postRepository, commentRepository)

        mockUser1 = mock(User::class.java)
        mockUser2 = mock(User::class.java)
        mockPost1 = mock(Post::class.java)
        mockPost2 = mock(Post::class.java)
        mockComment1 = mock(Comment::class.java)
        mockComment2 = mock(Comment::class.java)
        `when`(mockUser1.id).thenReturn(1L)
        `when`(mockUser2.id).thenReturn(2L)
        `when`(mockPost1.id).thenReturn(1L)
        `when`(mockPost1.author).thenReturn(mockUser1)
        `when`(mockPost1.title).thenReturn("mockPostTitle1")
        `when`(mockPost1.content).thenReturn("mockPostContent1")
        `when`(mockPost2.id).thenReturn(2L)
        `when`(mockPost2.author).thenReturn(mockUser2)
        `when`(mockPost2.title).thenReturn("mockPostTitle2")
        `when`(mockPost2.content).thenReturn("mockPostContent2")
        `when`(mockComment1.id).thenReturn(1L)
        `when`(mockComment1.post).thenReturn(mockPost1)
        `when`(mockComment1.content).thenReturn("mockComment1.content")
        `when`(mockComment1.author).thenReturn(mockUser1)
        `when`(mockComment2.id).thenReturn(2L)
        `when`(mockComment2.post).thenReturn(mockPost1)
        `when`(mockComment2.author).thenReturn(mockUser1)
        `when`(mockComment2.content).thenReturn("mockComment2.content")

        `when`(postRepository.findById(mockPost1.id)).thenReturn(Optional.of(mockPost1))
        `when`(userRepository.findById(mockUser1.id)).thenReturn(Optional.of(mockUser1))
        `when`(userRepository.findById(mockUser2.id)).thenReturn(Optional.of(mockUser2))
        `when`(commentRepository.findById(mockComment1.id)).thenReturn(Optional.of(mockComment1))
        `when`(commentRepository.findById(mockComment2.id)).thenReturn(Optional.of(mockComment2))
    }

    @Test
    fun getPostsTest() {
        val pageNumber = 0 // 첫 번째 페이지 (0부터 시작)
        val pageSize = 10  // 페이지당 데이터 개수
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)

        val posts = listOf(mockPost1, mockPost2)
        `when`(postRepository.findAll(pageable)).thenReturn(PageImpl(posts))

        val result = postQueryService.getPosts(pageable)

        assertEquals(2, result.size)
        assertEquals(posts[0].id, result.content[0].id)
        assertEquals(posts[0].title, result.content[0].title)
        assertEquals(posts[0].content, result.content[0].content)
        assertEquals(posts[1].id, result.content[1].id)
        assertEquals(posts[1].title, result.content[1].title)
        assertEquals(posts[1].content, result.content[1].content)

        verify(postRepository, times(1)).findAll(pageable)
    }

    @Test
    fun getPostsFeed() {
        val pageNumber = 0 // 첫 번째 페이지 (0부터 시작)
        val pageSize = 5  // 페이지당 데이터 개수
        val pageSizePlusOne = pageSize + 1
        val pageable: Pageable = PageRequest.of(pageNumber, pageSizePlusOne)

        val posts = listOf(mockPost1, mockPost2)

        `when`(postRepository.findTopNById(pageSizePlusOne.toLong())).thenReturn(posts)
        `when`(postRepository.findNextPageByIdLessThanOrderByIdDesc(mockPost2.id, pageable)).thenReturn(listOf(mockPost1))

        // cursor 가 없는경우
        val result1 = postQueryService.getPostsFeed(null)

        assertEquals(2, result1.content.size)
        assertEquals(posts[0].id, result1.content[0].id)
        assertEquals(posts[0].title, result1.content[0].title)
        assertEquals(posts[0].content, result1.content[0].content)
        assertEquals(posts[1].id, result1.content[1].id)
        assertEquals(posts[1].title, result1.content[1].title)
        assertEquals(posts[1].content, result1.content[1].content)

        verify(postRepository, times(1)).findTopNById(pageSizePlusOne.toLong())

        // cursor 가 있는경우
        val result2 = postQueryService.getPostsFeed(mockPost2.id)

        assertEquals(1, result2.content.size)
        assertEquals(posts[0].id, result2.content[0].id)
        assertEquals(posts[0].title, result2.content[0].title)
        assertEquals(posts[0].content, result2.content[0].content)

        verify(postRepository, times(1)).findNextPageByIdLessThanOrderByIdDesc(mockPost2.id, pageable)
    }

    @Test
    fun getPostByIdTest() {
        `when`(postRepository.findById(mockPost1.id)).thenReturn(Optional.of(mockPost1))

        val result = postQueryService.getPostById(mockPost1.id)

        assertEquals(mockPost1.id, result.id)
        assertEquals(mockPost1.title, result.title)
        assertEquals(mockPost1.content, result.content)
        assertEquals(mockPost1.author.id, result.authorId)

        verify(postRepository, times(1)).findById(mockPost1.id)
    }

    @Test
    fun getPostByIdNotFoundTest() {
        val postId = 1L

        `when`(postRepository.findById(postId)).thenReturn(Optional.empty())

        org.junit.jupiter.api.assertThrows<NotFoundException> { postQueryService.getPostById(postId) }

        verify(postRepository, times(1)).findById(postId)
    }

    @Test
    fun getCommentsByPostId() {
        val pageNumber = 0 // 첫 번째 페이지 (0부터 시작)
        val pageSize = 10  // 페이지당 데이터 개수
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)

        val postId = 1L
        `when`(commentRepository.findByPostId(postId, pageable)).thenReturn(PageImpl(listOf(mockComment1, mockComment2)))

        val result = postQueryService.getCommentsByPostId(postId, pageable)

        assertEquals(mockComment1.id, result.content[0].id)
        assertEquals(mockComment1.post.id, result.content[0].postId)
        assertEquals(mockComment1.content, result.content[0].content)
        assertEquals(mockComment1.author.id, result.content[0].authorId)
        assertEquals(mockComment2.id, result.content[1].id)
        assertEquals(mockComment2.post.id, result.content[1].postId)
        assertEquals(mockComment2.content, result.content[1].content)
        assertEquals(mockComment2.author.id, result.content[1].authorId)

        verify(commentRepository, times(1)).findByPostId(postId, pageable)
    }
}