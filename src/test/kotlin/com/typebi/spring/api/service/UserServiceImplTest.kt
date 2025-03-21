package com.typebi.spring.api.service

import com.typebi.spring.api.requests.UserCreateDTO
import com.typebi.spring.api.requests.UserUpdateDTO
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.entity.Post
import com.typebi.spring.db.entity.User
import com.typebi.spring.db.repository.PostRepository
import com.typebi.spring.db.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class UserServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var postRepository: PostRepository
    private lateinit var userService: UserService
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    private lateinit var mockUser1: User
    private lateinit var mockUser2: User
    private lateinit var mockPost1: Post
    private lateinit var mockPost2: Post

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        postRepository = mock(PostRepository::class.java)
        passwordEncoder = BCryptPasswordEncoder()
        userService = UserServiceImpl(userRepository, postRepository)

        mockUser1 = mock(User::class.java)
        mockUser2 = mock(User::class.java)
        mockPost1 = mock(Post::class.java)
        mockPost2 = mock(Post::class.java)
        `when`(mockUser1.id).thenReturn(1L)
        `when`(mockUser1.username).thenReturn("username1")
        `when`(mockUser1.email).thenReturn("email1@email.com")
        `when`(mockUser2.id).thenReturn(2L)
        `when`(mockUser2.username).thenReturn("username2")
        `when`(mockUser2.email).thenReturn("email2@email.com")
        `when`(mockPost1.id).thenReturn(1L)
        `when`(mockPost1.author).thenReturn(mockUser1)
        `when`(mockPost1.title).thenReturn("mockPostTitle1")
        `when`(mockPost1.content).thenReturn("mockPostContent1")
        `when`(mockPost2.id).thenReturn(2L)
        `when`(mockPost2.author).thenReturn(mockUser2)
        `when`(mockPost2.title).thenReturn("mockPostTitle2")
        `when`(mockPost2.content).thenReturn("mockPostContent2")
    }

    @Test
    fun createUserTest() {
        val userCreateDTO1 = UserCreateDTO(username = mockUser1.username, password = mockUser1.email, email = mockUser1.email)

        `when`(userRepository.save(any(User::class.java))).thenReturn(mockUser1)

        val result = userService.createUser(userCreateDTO1)

        assertEquals(userCreateDTO1.username, result.username)
        assertEquals(userCreateDTO1.email, result.email)
        verify(userRepository, times(1)).save(any(User::class.java))
    }

    @Test
    fun getUsersTest() {
        `when`(userRepository.findAll()).thenReturn(listOf(mockUser1, mockUser2))

        val result = userService.getUsers()

        assertEquals(2, result.size)
        assertEquals(mockUser1.username, result[0].username)
        assertEquals(mockUser1.email, result[0].email)
        assertEquals(mockUser2.username, result[1].username)
        assertEquals(mockUser2.email, result[1].email)
        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun getUserByIdTest() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1))

        val result = userService.getUserById(1L)

        assertEquals(mockUser1.username, result.username)
        assertEquals(mockUser1.email, result.email)
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun getUserByIdNotFoundTest() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { userService.getUserById(1L) }
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun getPostsByUserId() {

        `when`(userRepository.findById(mockUser1.id)).thenReturn(Optional.of(mockUser1))
        `when`(postRepository.findByAuthorId(mockUser1.id)).thenReturn(listOf(mockPost1))

        val result = userService.getPostsByUserId(mockUser1.id)

        result.forEach {
            assertEquals(mockUser1.id, it.authorId)
        }

        verify(postRepository, times(1)).findByAuthorId(mockUser1.id)
    }

    @Test
    fun updateUserByIdTest() {
        val userUpdateDTO = UserUpdateDTO(username = "updatedName", password = "newPassword", email = "updated@email.com")

        `when`(mockUser1.username).thenReturn("updatedName")
        `when`(mockUser1.email).thenReturn("updated@email.com")

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1))

        val result = userService.updateUserById(1L, userUpdateDTO)

        assertEquals(mockUser1.username, result.username)
        assertEquals(mockUser1.email, result.email)
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun updateUserByIdNotFoundTest() {
        val userUpdateDTO = UserUpdateDTO(username = "updatedName", password = "newPassword", email = "updated@email.com")
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { userService.updateUserById(1L, userUpdateDTO) }
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun deleteUserByIdTest() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1))
        doNothing().`when`(userRepository).delete(mockUser1)

        val result = userService.deleteUserById(1L)

        assertTrue(result)
        verify(userRepository, times(1)).findById(1L)
        verify(userRepository, times(1)).delete(mockUser1)
    }

    @Test
    fun deleteUserByIdNotFoundTest() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { userService.deleteUserById(1L) }
        verify(userRepository, times(1)).findById(1L)
    }
}