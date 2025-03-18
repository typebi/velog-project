package com.typebi.spring.api.service

import com.typebi.spring.api.requests.UserCreateDTO
import com.typebi.spring.api.requests.UserUpdateDTO
import com.typebi.spring.api.responses.UserResponseDTO
import com.typebi.spring.api.responses.userResponseDTOFrom
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.entity.userOf
import com.typebi.spring.db.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    @Transactional
    override fun createUser(userCreateDTO: UserCreateDTO): UserResponseDTO {
        return userResponseDTOFrom(userRepository.save(userOf(userCreateDTO)))
    }

    override fun getUsers(): List<UserResponseDTO> {
        return userRepository.findAll().map { userResponseDTOFrom(it) }
    }

    override fun getUserById(id: Long): UserResponseDTO {
        val user = userRepository.findById(id).orElseThrow { NotFoundException("User not found with id : $id")}
        return userResponseDTOFrom(user)
    }

    @Transactional
    override fun updateUserById(id: Long, userUpdateDTO: UserUpdateDTO): UserResponseDTO {
        val user = userRepository.findById(id).orElseThrow { NotFoundException("User not found with id : $id")}

        userUpdateDTO.username?.let { user.username = it }
        userUpdateDTO.password?.let { user.password = BCryptPasswordEncoder().encode(it) }
        userUpdateDTO.email?.let { user.email = it }

        return userResponseDTOFrom(user)
    }

    @Transactional
    override fun deleteUserById(id: Long): Boolean {
        val user = userRepository.findById(id).orElseThrow { NotFoundException("User not found with id : $id")}
        userRepository.delete(user)
        return true
    }
}