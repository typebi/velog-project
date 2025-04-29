package com.typebi.spring.user.service

import com.typebi.spring.common.exception.UnauthorizedException
import com.typebi.spring.user.repository.UserRepository
import com.typebi.spring.user.requests.LoginRequestDTO
import com.typebi.spring.user.responses.LoginResponseDTO
import com.typebi.spring.utils.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder
) : AuthService {
    override fun login(request: LoginRequestDTO): LoginResponseDTO {
        val user = userRepository.findByEmail(request.email)
            ?: throw UnauthorizedException("존재하지 않는 사용자입니다.")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw UnauthorizedException("비밀번호가 일치하지 않습니다.")
        }

        val token = jwtProvider.generateToken(user.publicId, user.email)
        return LoginResponseDTO(
            token = token,
            publicId = user.publicId
        )
    }
}