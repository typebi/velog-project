package com.typebi.spring.api.service

import com.typebi.spring.api.requests.CommentCreateDTO
import com.typebi.spring.api.requests.PostCreateDTO
import com.typebi.spring.api.requests.PostUpdateDTO
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.entity.*
import com.typebi.spring.db.repository.CommentRepository
import com.typebi.spring.db.repository.PostRepository
import com.typebi.spring.db.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*

class PostServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var postRepository: PostRepository
    private lateinit var commentRepository: CommentRepository
    private lateinit var postService: PostService

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
        postService = PostServiceImpl(userRepository, postRepository, commentRepository)

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
    fun createPostTest() {
        `when`(userRepository.findById(mockUser1.id)).thenReturn(Optional.of(mockUser1))
        `when`(postRepository.save(any())).thenReturn(mockPost1)

        val result = postService.createPost(PostCreateDTO("title", "content", mockUser1.id))

        assertEquals(mockPost1.id, result.id)
        assertEquals(mockPost1.title, result.title)
        assertEquals(mockPost1.content, result.content)
        assertEquals(mockPost1.author.id, result.authorId)

        verify(userRepository, times(1)).findById(mockUser1.id)
        verify(postRepository, times(1)).save(any())
    }

    @Test
    fun createPostNotFoundTest() {
        val authorId = 1L
        val postCreateDTO = PostCreateDTO("Test Title", "Test Content", authorId)

        `when`(userRepository.findById(authorId)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { postService.createPost(postCreateDTO) }

        verify(userRepository, times(1)).findById(authorId)
        verify(postRepository, times(0)).save(any())
    }

    @Test
    fun getPostsTest() {
        val posts = listOf(mockPost1, mockPost2)

        `when`(postRepository.findAll()).thenReturn(posts)

        val result = postService.getPosts()

        assertEquals(2, result.size)
        assertEquals(posts[0].id, result[0].id)
        assertEquals(posts[0].title, result[0].title)
        assertEquals(posts[0].content, result[0].content)
        assertEquals(posts[1].id, result[1].id)
        assertEquals(posts[1].title, result[1].title)
        assertEquals(posts[1].content, result[1].content)

        verify(postRepository, times(1)).findAll()
    }

    @Test
    fun getPostByIdTest() {
        `when`(postRepository.findById(mockPost1.id)).thenReturn(Optional.of(mockPost1))

        val result = postService.getPostById(mockPost1.id)

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

        assertThrows<NotFoundException> { postService.getPostById(postId) }

        verify(postRepository, times(1)).findById(postId)
    }

    @Test
    fun getCommentsByPostId() {
        val postId = 1L
        val stubbedComment1 = commentOf(CommentCreateDTO(postId, "testcontent1", mockUser1.id), mockPost1, mockUser1)
        val stubbedComment2 = commentOf(CommentCreateDTO(postId, "testcontent2", mockUser1.id), mockPost1, mockUser2)

        `when`(commentRepository.findByPostId(postId)).thenReturn(listOf(stubbedComment1, stubbedComment2))


        val result = postService.getCommentsByPostId(postId)

        assertEquals(stubbedComment1.id, result[0].id)
        assertEquals(stubbedComment1.post.id, result[0].postId)
        assertEquals(stubbedComment1.content, result[0].content)
        assertEquals(stubbedComment1.author.id, result[0].authorId)
        assertEquals(stubbedComment2.id, result[1].id)
        assertEquals(stubbedComment2.post.id, result[1].postId)
        assertEquals(stubbedComment2.content, result[1].content)
        assertEquals(stubbedComment2.author.id, result[1].authorId)

        verify(commentRepository, times(1)).findByPostId(postId)
    }

    @Test
    fun updatePostByIdTest() {
        val postUpdateDTO1 = PostUpdateDTO(title = "title1", content = "content1", authorId = mockUser1.id)

        `when`(mockPost1.author).thenReturn(mockUser1)
        `when`(mockPost1.title).thenReturn(postUpdateDTO1.title)
        `when`(mockPost1.content).thenReturn(postUpdateDTO1.content)

        `when`(postRepository.findById(mockPost1.id)).thenReturn(Optional.of(mockPost1))
        `when`(userRepository.findById(mockUser1.id)).thenReturn(Optional.of(mockUser1))

        val result = postService.updatePostById(mockPost1.id, postUpdateDTO1)

        assertEquals(mockPost1.id, result.id)
        assertEquals(postUpdateDTO1.title, result.title)
        assertEquals(postUpdateDTO1.content, result.content)
        assertEquals(mockUser1.id, result.authorId)

        verify(postRepository, times(1)).findById(mockPost1.id)
        verify(userRepository,times(1)).findById(mockUser1.id)
    }

    @Test
    fun updatePostByIdNotFoundTest() {
        val postUpdateDTO2 = PostUpdateDTO(title = "title2", content = "content2", authorId = mockUser1.id)

        `when`(postRepository.findById(mockPost2.id)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { postService.updatePostById(mockPost2.id, postUpdateDTO2) }

        verify(postRepository, times(1)).findById(mockPost2.id)
    }

    @Test
    fun deletePostByIdTest() {
        `when`(postRepository.findById(mockPost1.id)).thenReturn(Optional.of(mockPost1))
        doNothing().`when`(postRepository).delete(mockPost1)

        val result = postService.deletePostById(mockPost1.id)

        assertTrue(result)

        verify(postRepository, times(1)).findById(mockPost1.id)
        verify(postRepository, times(1)).delete(mockPost1)
    }

    @Test
    fun deletePostByIdNotFoundTest() {
        val postId = 1L

        `when`(postRepository.findById(postId)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { postService.deletePostById(postId) }

        verify(postRepository, times(1)).findById(postId)
        verify(postRepository, times(0)).delete(any())
    }
}