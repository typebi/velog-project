package com.typebi.spring.db.entity

import com.typebi.spring.post.requests.PostCreateDTO
import jakarta.persistence.*

@Entity
@Table(name = "posts")
class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0

    @Column(nullable = false)
    lateinit var title: String

    @Column(nullable = false, columnDefinition = "TEXT")
    lateinit var content: String

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    lateinit var author: User
}

fun postOf(postCreateDTO: PostCreateDTO, author: User): Post {
    return Post().apply {
        this.title = postCreateDTO.title
        this.content = postCreateDTO.content
        this.author = author
    }
}