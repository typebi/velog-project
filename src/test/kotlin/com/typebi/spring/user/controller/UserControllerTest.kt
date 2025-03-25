package com.typebi.spring.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.typebi.spring.user.requests.UserCreateDTO
import com.typebi.spring.user.requests.UserUpdateDTO
import com.typebi.spring.user.responses.userResponseDTOFrom
import com.typebi.spring.user.service.UserService
import com.typebi.spring.db.entity.User
import com.typebi.spring.db.entity.userOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var userService: UserService

    private lateinit var stubbedUser1: User
    private lateinit var stubbedUser2: User

    @BeforeEach
    fun setUp() {
        stubbedUser1 = userOf(UserCreateDTO(username = "testname1", password = "1234", email = "testmail1"))
        stubbedUser2 = userOf(UserCreateDTO(username = "testname2", password = "1234", email = "testmail2"))
    }

    @Test
    fun createUserTest() {
        val userCreateDTO = UserCreateDTO(username = "username", password = "1234", email = "testmail")
        val createdUser = userOf(userCreateDTO)
        `when`(userService.createUser(userCreateDTO)).thenReturn(userResponseDTOFrom(createdUser))

        mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userCreateDTO)))
//            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.username").value(userCreateDTO.username))
            .andExpect(jsonPath("$.data.email").value(userCreateDTO.email))
    }

    @Test
    fun getUsersTest() {
        `when`(userService.getUsers()).thenReturn(listOf(stubbedUser1, stubbedUser2).map { userResponseDTOFrom(it) })

        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.[0].username").value(stubbedUser1.username))
            .andExpect(jsonPath("$.data.[0].email").value(stubbedUser1.email))
            .andExpect(jsonPath("$.data.[1].username").value(stubbedUser2.username))
            .andExpect(jsonPath("$.data.[1].email").value(stubbedUser2.email))
    }

    @Test
    fun getUserByIdTest() {
        `when`(userService.getUserById(stubbedUser1.id)).thenReturn(userResponseDTOFrom(stubbedUser1))

        mockMvc.perform(get("/api/v1/users/${stubbedUser1.id}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.username").value(stubbedUser1.username))
            .andExpect(jsonPath("$.data.email").value(stubbedUser1.email))
    }

    @Test
    fun updateUserByIdTest() {
        val userUpdateDTO = UserUpdateDTO(username = "testname", password = "1234", email = "testmail")
        `when`(userService.updateUserById(stubbedUser2.id, userUpdateDTO))
            .thenReturn(userResponseDTOFrom(userOf(UserCreateDTO(username = "testname", password = "1234", email = "testmail"))))

        mockMvc.perform(patch("/api/v1/users/${stubbedUser2.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userUpdateDTO)))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.username").value(userUpdateDTO.username))
            .andExpect(jsonPath("$.data.email").value(userUpdateDTO.email))
    }

    @Test
    fun deleteUserByIdTest() {
        val stubbedUser = userOf(UserCreateDTO(username = "testname", password = "1234", email = "testmail"))

        mockMvc.perform(delete("/api/v1/users/${stubbedUser.id}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
    }
}