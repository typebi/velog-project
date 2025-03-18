package com.typebi.spring.db.entity

import com.typebi.spring.api.requests.UserCreateDTO
import jakarta.persistence.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Entity
@Table(name = "users")
class User: BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0

    @Column(nullable = false, unique = true)
    lateinit var username: String

    @Column(nullable = false)
    lateinit var password: String

    @Column(nullable = false, unique = true)
    lateinit var email: String
}

fun userOf(userCreateDTO: UserCreateDTO): User {
    return User().apply {
        this.username = userCreateDTO.username
        this.password = BCryptPasswordEncoder().encode(userCreateDTO.password)
        this.email = userCreateDTO.email
    }
}