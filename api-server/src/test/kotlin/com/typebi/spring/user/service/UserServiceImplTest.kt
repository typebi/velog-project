package com.typebi.spring.user.service

import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.post.model.Post
import com.typebi.spring.user.model.User
import com.typebi.spring.post.repository.PostRepository
import com.typebi.spring.user.repository.UserRepository
import com.typebi.spring.user.requests.UserCreateDTO
import com.typebi.spring.user.requests.UserUpdateDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
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
        userRepository = Mockito.mock(UserRepository::class.java)
        postRepository = Mockito.mock(PostRepository::class.java)
        passwordEncoder = BCryptPasswordEncoder()
        userService = UserServiceImpl(userRepository, postRepository)

        mockUser1 = Mockito.mock(User::class.java)
        mockUser2 = Mockito.mock(User::class.java)
        mockPost1 = Mockito.mock(Post::class.java)
        mockPost2 = Mockito.mock(Post::class.java)
        Mockito.`when`(mockUser1.id).thenReturn(1L)
        Mockito.`when`(mockUser1.username).thenReturn("username1")
        Mockito.`when`(mockUser1.email).thenReturn("email1@email.com")
        Mockito.`when`(mockUser2.id).thenReturn(2L)
        Mockito.`when`(mockUser2.username).thenReturn("username2")
        Mockito.`when`(mockUser2.email).thenReturn("email2@email.com")
        Mockito.`when`(mockPost1.id).thenReturn(1L)
        Mockito.`when`(mockPost1.author).thenReturn(mockUser1)
        Mockito.`when`(mockPost1.title).thenReturn("mockPostTitle1")
        Mockito.`when`(mockPost1.content).thenReturn("mockPostContent1")
        Mockito.`when`(mockPost2.id).thenReturn(2L)
        Mockito.`when`(mockPost2.author).thenReturn(mockUser2)
        Mockito.`when`(mockPost2.title).thenReturn("mockPostTitle2")
        Mockito.`when`(mockPost2.content).thenReturn("mockPostContent2")
    }

    @Test
    fun createUserTest() {
        val userCreateDTO1 =
            UserCreateDTO(username = mockUser1.username, password = mockUser1.email, email = mockUser1.email)

        Mockito.`when`(userRepository.save(any(User::class.java))).thenReturn(mockUser1)

        val result = userService.createUser(userCreateDTO1)

        Assertions.assertEquals(userCreateDTO1.username, result.username)
        Assertions.assertEquals(userCreateDTO1.email, result.email)
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User::class.java))
    }

    @Test
    fun getUsersTest() {
        Mockito.`when`(userRepository.findAll()).thenReturn(listOf(mockUser1, mockUser2))

        val result = userService.getUsers()

        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(mockUser1.username, result[0].username)
        Assertions.assertEquals(mockUser1.email, result[0].email)
        Assertions.assertEquals(mockUser2.username, result[1].username)
        Assertions.assertEquals(mockUser2.email, result[1].email)
        Mockito.verify(userRepository, Mockito.times(1)).findAll()
    }

    @Test
    fun getUserByIdTest() {
        Mockito.`when`(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1))

        val result = userService.getUserById(1L)

        Assertions.assertEquals(mockUser1.username, result.username)
        Assertions.assertEquals(mockUser1.email, result.email)
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L)
    }

    @Test
    fun getUserByIdNotFoundTest() {
        Mockito.`when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { userService.getUserById(1L) }
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L)
    }

    @Test
    fun getPostsByUserId() {

        Mockito.`when`(userRepository.findById(mockUser1.id)).thenReturn(Optional.of(mockUser1))
        Mockito.`when`(postRepository.findByAuthorId(mockUser1.id)).thenReturn(listOf(mockPost1))

        val result = userService.getPostsByUserId(mockUser1.id)

        result.forEach {
            Assertions.assertEquals(mockUser1.id, it.authorId)
        }

        Mockito.verify(postRepository, Mockito.times(1)).findByAuthorId(mockUser1.id)
    }

    @Test
    fun updateUserByIdTest() {
        val userUpdateDTO =
            UserUpdateDTO(username = "updatedName", password = "newPassword", email = "updated@email.com")

        Mockito.`when`(mockUser1.username).thenReturn("updatedName")
        Mockito.`when`(mockUser1.email).thenReturn("updated@email.com")

        Mockito.`when`(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1))

        val result = userService.updateUserById(1L, userUpdateDTO)

        Assertions.assertEquals(mockUser1.username, result.username)
        Assertions.assertEquals(mockUser1.email, result.email)
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L)
    }

    @Test
    fun updateUserByIdNotFoundTest() {
        val userUpdateDTO =
            UserUpdateDTO(username = "updatedName", password = "newPassword", email = "updated@email.com")
        Mockito.`when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { userService.updateUserById(1L, userUpdateDTO) }
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L)
    }

    @Test
    fun deleteUserByIdTest() {
        Mockito.`when`(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1))
        Mockito.doNothing().`when`(userRepository).delete(mockUser1)

        val result = userService.deleteUserById(1L)

        Assertions.assertTrue(result)
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L)
        Mockito.verify(userRepository, Mockito.times(1)).delete(mockUser1)
    }

    @Test
    fun deleteUserByIdNotFoundTest() {
        Mockito.`when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { userService.deleteUserById(1L) }
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L)
    }
}