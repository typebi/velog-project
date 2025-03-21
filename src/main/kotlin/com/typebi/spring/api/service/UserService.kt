package com.typebi.spring.api.service

import com.typebi.spring.api.responses.UserResponseDTO
import com.typebi.spring.api.requests.UserCreateDTO
import com.typebi.spring.api.requests.UserUpdateDTO
import com.typebi.spring.api.responses.PostResponseDTO
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun createUser(userCreateDTO: UserCreateDTO): UserResponseDTO
    fun getUsers(): List<UserResponseDTO>
    fun getUserById(id: Long): UserResponseDTO
    fun getPostsByUserId(id: Long): List<PostResponseDTO>
    fun updateUserById(id: Long, userUpdateDTO: UserUpdateDTO): UserResponseDTO
    fun deleteUserById(id: Long): Boolean

}
