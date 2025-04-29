package com.typebi.spring.user.controller

import com.typebi.spring.user.requests.UserCreateDTO
import com.typebi.spring.user.requests.UserUpdateDTO
import com.typebi.spring.post.responses.PostResponseDTO
import com.typebi.spring.user.responses.UserResponseDTO
import com.typebi.spring.user.service.UserService
import com.typebi.spring.common.exception.BadRequestException
import com.typebi.spring.common.response.ApiResponse
import org.springframework.hateoas.server.mvc.linkTo
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
        userDTO.add(
            linkTo<UserController> { getUserById(userId) }.withSelfRel(),
            linkTo<UserController> { updateUserById(userId, null) }.withRel("update"),
            linkTo<UserController> { deleteUserById(userId) }.withRel("delete"),
            linkTo<UserController> { getPostsByUserId(userId) }.withRel("posts")
        )
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
        @RequestBody userUpdateDTO: UserUpdateDTO?
    ): ResponseEntity<ApiResponse<UserResponseDTO>> {
        if (userUpdateDTO == null) {
            throw BadRequestException("Request body is null")
        }
        val userDTO = userService.updateUserById(userId, userUpdateDTO)
        val response = ApiResponse(true, userDTO, "User with ID $userId updated successfully", HttpStatus.OK.value())
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{userId:\\d+}")
    fun deleteUserById(@PathVariable(name = "userId") userId: Long): ResponseEntity<ApiResponse<Unit>> {
        val result = userService.deleteUserById(userId)
        val response =
            ApiResponse(result, Unit, "User with ID $userId deleted successfully", HttpStatus.NO_CONTENT.value())
        return ResponseEntity.ok(response)
    }
}