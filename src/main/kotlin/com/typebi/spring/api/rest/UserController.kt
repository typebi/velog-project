package com.typebi.spring.api.rest

import com.typebi.spring.api.requests.UserCreateDTO
import com.typebi.spring.api.requests.UserUpdateDTO
import com.typebi.spring.api.responses.UserResponseDTO
import com.typebi.spring.api.service.UserService
import com.typebi.spring.common.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun createUser(@RequestBody userCreateDTO: UserCreateDTO): ResponseEntity<ApiResponse<UserResponseDTO>> {
        val userDTO = userService.createUser(userCreateDTO)
        val location = URI.create("/api/v1/users/${userDTO.id}")
        val response = ApiResponse(true, userDTO, "User created successfully")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    fun getUsers(): ResponseEntity<ApiResponse<List<UserResponseDTO>>> {
        val userDTOs = userService.getUsers()
        val response = ApiResponse(true, userDTOs, "Users retrieved successfully")
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{userId:\\d+}")
    fun getUserById(@PathVariable(name = "userId") userId: Long): ResponseEntity<ApiResponse<UserResponseDTO>> {
        val userDTO = userService.getUserById(userId)
        val response = ApiResponse(true, userDTO, "User with ID $userId retrieved successfully")
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{userId:\\d+}")
    fun updateUserById(
        @PathVariable(name = "userId") userId: Long,
        @RequestBody userUpdateDTO: UserUpdateDTO
    ): ResponseEntity<ApiResponse<UserResponseDTO>> {
        val userDTO = userService.updateUserById(userId, userUpdateDTO)
        val response = ApiResponse(true, userDTO, "User with ID $userId updated successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{userId:\\d+}")
    fun deleteUserById(@PathVariable(name = "userId") userId: Long): ResponseEntity<Void> {
        userService.deleteUserById(userId)
        return ResponseEntity.noContent().build()
    }
}