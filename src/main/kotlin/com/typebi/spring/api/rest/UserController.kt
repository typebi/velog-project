package com.typebi.spring.api.rest

import com.typebi.spring.api.requests.UserCreateDTO
import com.typebi.spring.api.requests.UserUpdateDTO
import com.typebi.spring.api.responses.UserResponseDTO
import com.typebi.spring.api.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun createUser(@RequestBody userCreateDTO: UserCreateDTO): UserResponseDTO {
        return userService.createUser(userCreateDTO)
    }
    @GetMapping
    fun getUsers(): List<UserResponseDTO> {
        return userService.getUsers()
    }

    @GetMapping("/{userId:\\d+}")
    fun getUserById(@PathVariable(name = "userId") userId: Long): UserResponseDTO {
        return userService.getUserById(userId)
    }

    @PatchMapping("/{userId:\\d+}")
    fun updateUserById(
        @PathVariable(name = "userId") userId: Long,
        @RequestBody userUpdateDTO: UserUpdateDTO
    ): UserResponseDTO {
        return userService.updateUserById(userId, userUpdateDTO)
    }

    @DeleteMapping("/{userId:\\d+}")
    fun deleteUserById(@PathVariable(name = "userId") userId: Long) {
        userService.deleteUserById(userId)
    }
}