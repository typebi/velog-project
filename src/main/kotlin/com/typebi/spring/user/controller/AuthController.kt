package com.typebi.spring.user.controller

import com.typebi.spring.common.response.ApiResponse
import com.typebi.spring.user.requests.LoginRequestDTO
import com.typebi.spring.user.responses.LoginResponseDTO
import com.typebi.spring.user.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<ApiResponse<LoginResponseDTO>> {
        val response = authService.login(request)
        return ResponseEntity.ok(ApiResponse(
            success = true,
            data = response,
            message = "로그인 성공",
            code = 200
        ))
    }
}