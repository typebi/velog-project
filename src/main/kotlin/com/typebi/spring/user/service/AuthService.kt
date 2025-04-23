package com.typebi.spring.user.service

import com.typebi.spring.user.requests.LoginRequestDTO
import com.typebi.spring.user.responses.LoginResponseDTO
import org.springframework.stereotype.Service

@Service
interface AuthService {
    fun login(request: LoginRequestDTO): LoginResponseDTO
}