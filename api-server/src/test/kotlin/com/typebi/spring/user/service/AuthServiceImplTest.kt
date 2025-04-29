package com.typebi.spring.user.service

import com.typebi.spring.user.model.User
import com.typebi.spring.user.repository.UserRepository
import com.typebi.spring.user.requests.LoginRequestDTO
import com.typebi.spring.user.responses.LoginResponseDTO
import com.typebi.spring.utils.JwtProvider
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceImplTest {
    private lateinit var userRepository: UserRepository
    private lateinit var jwtProvider: JwtProvider
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var authService: AuthService

    @BeforeEach
    fun setup() {
        userRepository = mock(UserRepository::class.java)
        jwtProvider = mock(JwtProvider::class.java)
        passwordEncoder = mock(PasswordEncoder::class.java)
        authService = AuthServiceImpl(userRepository, jwtProvider, passwordEncoder)
    }

    @Test
    fun loginSuccessTest() {
        // 로그인 성공 시 토큰과 publicId를 반환
        // given
        val email = "mockUser1@example.com"
        val rawPassword = "1234"
        val encodedPassword = "hashed1234"
        val publicId = "uuid-1234"
        val token = "jwt.token.value"
        val user = mock(User::class.java).apply {
            Mockito.`when`(this.email).thenReturn(email)
            Mockito.`when`(this.password).thenReturn(encodedPassword)
            Mockito.`when`(this.publicId).thenReturn(publicId)
        }

        Mockito.`when`(userRepository.findByEmail(email)).thenReturn(user)
        Mockito.`when`(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true)
        Mockito.`when`(jwtProvider.generateToken(publicId, email)).thenReturn(token)

        val request = LoginRequestDTO(email, rawPassword)

        // when
        val response: LoginResponseDTO = authService.login(request)

        // then
        assertEquals(token, response.token)
        assertEquals(publicId, response.publicId)
    }
}