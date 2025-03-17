package com.typebi.spring.api.service

import com.typebi.spring.api.requests.UserCreateDTO
import com.typebi.spring.api.requests.UserUpdateDTO
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.entity.User
import com.typebi.spring.db.entity.userOf
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
    private lateinit var userService: UserService
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    private lateinit var stubbedUser1: User
    private lateinit var stubbedUser2: User
    private lateinit var userCreateDTO1: UserCreateDTO
    private lateinit var userCreateDTO2: UserCreateDTO

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        passwordEncoder = BCryptPasswordEncoder()
        userService = UserServiceImpl(userRepository)

        userCreateDTO1 = UserCreateDTO(username = "testname1", password = "1234", email = "testmail1")
        userCreateDTO2 = UserCreateDTO(username = "testname2", password = "1234", email = "testmail2")
        stubbedUser1 = userOf(userCreateDTO1)
        stubbedUser2 = userOf(userCreateDTO2)
    }

    @Test
    fun createUserTest() {
        `when`(userRepository.save(any(User::class.java))).thenReturn(stubbedUser1)

        val result = userService.createUser(userCreateDTO1)

        assertEquals(userCreateDTO1.username, result.username)
        assertEquals(userCreateDTO1.email, result.email)
        verify(userRepository, times(1)).save(any(User::class.java))
    }

    @Test
    fun getUsersTest() {
        `when`(userRepository.findAll()).thenReturn(listOf(stubbedUser1, stubbedUser2))

        val result = userService.getUsers()

        assertEquals(2, result.size)
        assertEquals(userCreateDTO1.username, result[0].username)
        assertEquals(userCreateDTO2.email, result[1].email)
        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun getUserByIdTest() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(stubbedUser1))

        val result = userService.getUserById(1L)

        assertEquals(userCreateDTO1.username, result.username)
        assertEquals(userCreateDTO1.email, result.email)
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun getUserByIdNotFoundTest() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { userService.getUserById(1L) }
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun updateUserByIdTest() {
        val userUpdateDTO = UserUpdateDTO(username = "updatedName", password = "newPassword", email = "updated@email.com")
        val updatedUser = userOf(userCreateDTO1).apply {
            username = userUpdateDTO.username!!
            password = passwordEncoder.encode(userUpdateDTO.password!!)
            email = userUpdateDTO.email!!
        }
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(stubbedUser1))

        val result = userService.updateUserById(1L, userUpdateDTO)

        assertEquals(updatedUser.username, result.username)
        assertEquals(updatedUser.email, result.email)
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
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(stubbedUser1))
        doNothing().`when`(userRepository).delete(stubbedUser1)

        val result = userService.deleteUserById(1L)

        assertTrue(result)
        verify(userRepository, times(1)).findById(1L)
        verify(userRepository, times(1)).delete(stubbedUser1)
    }

    @Test
    fun deleteUserByIdNotFoundTest() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> { userService.deleteUserById(1L) }
        verify(userRepository, times(1)).findById(1L)
    }
}