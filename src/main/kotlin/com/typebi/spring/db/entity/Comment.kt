package com.typebi.spring.db.entity

import com.typebi.spring.api.requests.CommentCreateDTO
import jakarta.persistence.*

@Entity
@Table(name = "comments")
class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    lateinit var post: Post

    @Column(nullable = false, columnDefinition = "TEXT")
    lateinit var content: String

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    lateinit var author: User
}

fun commentOf(commentCreateDTO: CommentCreateDTO, post: Post, author: User): Comment {
    return Comment().apply {
        this.post = post
        this.content = commentCreateDTO.content
        this.author = author
    }
}