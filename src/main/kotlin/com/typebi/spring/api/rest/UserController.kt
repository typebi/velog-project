package com.typebi.spring.api.rest

import com.typebi.spring.api.requests.UserCreateDTO
import com.typebi.spring.api.requests.UserUpdateDTO
import com.typebi.spring.api.responses.PostResponseDTO
import com.typebi.spring.api.responses.UserResponseDTO
import com.typebi.spring.api.service.UserService
import com.typebi.spring.common.response.ApiResponse
import org.springframework.http.HttpStatus
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
        val response = ApiResponse(true, userDTO, "User created successfully", HttpStatus.CREATED.value())
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    fun getUsers(): ResponseEntity<ApiResponse<List<UserResponseDTO>>> {
        val userDTOs = userService.getUsers()
        val response = ApiResponse(true, userDTOs, "Users retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{userId:\\d+}")
    fun getUserById(@PathVariable(name = "userId") userId: Long): ResponseEntity<ApiResponse<UserResponseDTO>> {
        val userDTO = userService.getUserById(userId)
        val response = ApiResponse(true, userDTO, "User with ID $userId retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{userId:\\d+}/posts")
    fun getPostsByUserId(@PathVariable(name = "userId") userId: Long): ResponseEntity<ApiResponse<List<PostResponseDTO>>> {
        val postDTOs = userService.getPostsByUserId(userId)
        val response = ApiResponse(true, postDTOs, "User with ID $userId retrieved successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{userId:\\d+}")
    fun updateUserById(
        @PathVariable(name = "userId") userId: Long,
        @RequestBody userUpdateDTO: UserUpdateDTO
    ): ResponseEntity<ApiResponse<UserResponseDTO>> {
        val userDTO = userService.updateUserById(userId, userUpdateDTO)
        val response = ApiResponse(true, userDTO, "User with ID $userId updated successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{userId:\\d+}")
    fun deleteUserById(@PathVariable(name = "userId") userId: Long): ResponseEntity<ApiResponse<Unit>> {
        val result = userService.deleteUserById(userId)
        val response = ApiResponse(result, Unit, "User with ID $userId deleted successfully", HttpStatus.NO_CONTENT.value())
        return ResponseEntity.ok(response)
    }
}