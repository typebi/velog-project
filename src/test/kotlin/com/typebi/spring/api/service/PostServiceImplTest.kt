package com.typebi.spring.api.service

import com.typebi.spring.api.requests.PostCreateDTO
import com.typebi.spring.api.requests.PostUpdateDTO
import com.typebi.spring.api.requests.UserCreateDTO
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.entity.User
import com.typebi.spring.db.entity.postOf
import com.typebi.spring.db.entity.userOf
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
    private lateinit var postService: PostService

    private lateinit var mockUser1: User
    private lateinit var stubbedUser1: User
    private lateinit var stubbedUser2: User
    private lateinit var userCreateDTO1: UserCreateDTO
    private lateinit var userCreateDTO2: UserCreateDTO
    private lateinit var postCreateDTO1: PostCreateDTO
    private lateinit var postCreateDTO2: PostCreateDTO
    private lateinit var postUpdateDTO1: PostUpdateDTO
    private lateinit var postUpdateDTO2: PostUpdateDTO
    private lateinit var postUpdateDTO3: PostUpdateDTO

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        postRepository = mock(PostRepository::class.java)
        postService = PostServiceImpl(userRepository, postRepository)

        mockUser1 = mock(User::class.java)
        `when`(mockUser1.id).thenReturn(1L)
        userCreateDTO1 = UserCreateDTO(username = "testname1", password = "1234", email = "testmail1")
        userCreateDTO2 = UserCreateDTO(username = "testname2", password = "1234", email = "testmail2")
        stubbedUser1 = userOf(userCreateDTO1)
        stubbedUser2 = userOf(userCreateDTO2)

        postCreateDTO1 = PostCreateDTO(title = "title1", content = "content1", authorId = stubbedUser1.id)
        postCreateDTO2 = PostCreateDTO(title = "title2", content = "content2", authorId = stubbedUser1.id)
        postUpdateDTO1 = PostUpdateDTO(title = "title1", content = "content1", authorId = mockUser1.id)
        postUpdateDTO2 = PostUpdateDTO(title = "title2", content = "content2", authorId = mockUser1.id)
        postUpdateDTO3 = PostUpdateDTO(title = null, content = null, authorId = null)
    }

    @Test
    fun createPostTest() {
        val savedPost = postOf(postCreateDTO1, stubbedUser1)

        `when`(userRepository.findById(stubbedUser1.id)).thenReturn(Optional.of(stubbedUser1))
        `when`(postRepository.save(any())).thenReturn(savedPost)

        val result = postService.createPost(postCreateDTO1)

        assertEquals(savedPost.id, result.id)
        assertEquals(savedPost.title, result.title)
        assertEquals(savedPost.content, result.content)
        assertEquals(savedPost.author.id, result.authorId)

        verify(userRepository, times(1)).findById(stubbedUser1.id)
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
        val posts = listOf(
            postOf(postCreateDTO1, stubbedUser1),
            postOf(postCreateDTO2, stubbedUser1),
        )

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
        val post = postOf(postCreateDTO1, stubbedUser1)

        `when`(postRepository.findById(post.id)).thenReturn(Optional.of(post))

        val result = postService.getPostById(post.id)

        assertEquals(post.id, result.id)
        assertEquals(post.title, result.title)
        assertEquals(post.content, result.content)
        assertEquals(post.author.id, result.authorId)

        verify(postRepository, times(1)).findById(post.id)
    }

    @Test
    fun getPostByIdNotFoundTest() {
        val postId = 1L

        `when`(postRepository.findById(postId)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { postService.getPostById(postId) }

        verify(postRepository, times(1)).findById(postId)
    }

    @Test
    fun updatePostByIdTest() {
        val post = postOf(postCreateDTO1, mockUser1)

        `when`(postRepository.findById(post.id)).thenReturn(Optional.of(post))
        `when`(userRepository.findById(mockUser1.id)).thenReturn(Optional.of(mockUser1))

        val result = postService.updatePostById(post.id, postUpdateDTO1)

        assertEquals(post.id, result.id)
        assertEquals(postUpdateDTO1.title, result.title)
        assertEquals(postUpdateDTO1.content, result.content)
        assertEquals(mockUser1.id, result.authorId)

        verify(postRepository, times(1)).findById(post.id)
        verify(userRepository,times(1)).findById(mockUser1.id)
    }

    @Test
    fun updatePostByIdNotFoundTest() {
        val post = postOf(postCreateDTO1, stubbedUser1)
        `when`(postRepository.findById(stubbedUser2.id)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { postService.updatePostById(post.id, postUpdateDTO2) }

        verify(postRepository, times(1)).findById(post.id)
    }

    @Test
    fun deletePostByIdTest() {
        val post = postOf(postCreateDTO1, stubbedUser1)

        `when`(postRepository.findById(post.id)).thenReturn(Optional.of(post))
        doNothing().`when`(postRepository).delete(post)

        val result = postService.deletePostById(post.id)

        assertTrue(result)

        verify(postRepository, times(1)).findById(post.id)
        verify(postRepository, times(1)).delete(post)
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