package com.typebi.spring.comment.service

import com.typebi.spring.comment.requests.CommentCreateDTO
import com.typebi.spring.comment.requests.CommentUpdateDTO
import com.typebi.spring.db.entity.*
import com.typebi.spring.db.repository.CommentRepository
import com.typebi.spring.db.repository.PostRepository
import com.typebi.spring.db.repository.UserRepository
import com.typebi.spring.post.requests.PostCreateDTO
import com.typebi.spring.post.service.PostQueryService
import com.typebi.spring.user.requests.UserCreateDTO
import com.typebi.spring.utils.QueryCounterAspect
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class CommentServiceImplIntegrationTest {
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var postRepository: PostRepository
    @Autowired
    lateinit var commentRepository: CommentRepository
    @Autowired
    lateinit var postQueryService: PostQueryService
    @Autowired
    lateinit var commentService: CommentService
    @Autowired
    lateinit var cacheManager: CacheManager
    @Autowired
    lateinit var queryCounterAspect: QueryCounterAspect

    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var post1: Post
    private lateinit var post2: Post
    private lateinit var comment1: Comment
    private lateinit var comment2: Comment

    @BeforeEach
    fun setUp() {
        // 테스트 데이터 생성 및 저장
        user1 = userRepository.save(userOf(UserCreateDTO(username = "user1", password = "1234", email = "user1@example.com")))
        post1 = postRepository.save(postOf(PostCreateDTO(title = "PostTitle1", content = "PostContent1", authorId = user1.id), user1))
        comment1 = commentRepository.save(commentOf(CommentCreateDTO(postId = post1.id, content = "Comment1.content", authorId = user1.id), post1, user1))

        user2 = userRepository.save(userOf(UserCreateDTO(username = "user2", password = "1234", email = "user2@example.com")))
        post2 = postRepository.save(postOf(PostCreateDTO(title = "PostTitle2", content = "PostContent2", authorId = user2.id), user2))
        comment2 = commentRepository.save(commentOf(CommentCreateDTO(postId = post1.id, content = "Comment2.content", authorId = user1.id), post1, user1))

    }

    @AfterEach
    fun clear() {
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()

        // 쿼리 카운트 초기화
        queryCounterAspect.resetCount()

        // 캐시 초기화
        cacheManager.getCache("posts")?.clear()
        cacheManager.getCache("commentsByPost")?.clear()
    }

    @Test
    fun updateCommentByIdCacheEvictTest() {
        // 처음 호출 시에는 캐시가 비어 있으므로 데이터베이스에서 조회
        val comments = postQueryService.getCommentsByPostId(post1.id)
        assertNotNull(comments)
        assertEquals(2, comments.size)
        assertEquals(1, queryCounterAspect.queryCount)

        // 캐시에 저장되어 있는지 확인
        val cachedComments1 = cacheManager.getCache("commentsByPost")?.get(post1.id, List::class.java)
        assertEquals(comments, cachedComments1)

        // 업데이트
        val result = commentService.updateCommentById(comment1.id, CommentUpdateDTO(null, "updatedContent", null))
        assertNotNull(result)
        assertEquals(1, queryCounterAspect.queryCount)

        // 캐시에서 삭제가 됐는지 확인
        val cachedComments2 = cacheManager.getCache("commentsByPost")?.get(post1.id, List::class.java)
        assertNull(cachedComments2)
        assertEquals(1, queryCounterAspect.queryCount)
    }

    @Test
    fun deleteCommentByIdCacheEvictTest() {
        // 처음 호출 시에는 캐시가 비어 있으므로 데이터베이스에서 조회
        val comments = postQueryService.getCommentsByPostId(post1.id)
        assertNotNull(comments)
        assertEquals(2, comments.size)
        assertEquals(1, queryCounterAspect.queryCount)

        // 캐시에 저장되어 있는지 확인
        val cachedComments1 = cacheManager.getCache("commentsByPost")?.get(post1.id, List::class.java)
        assertEquals(comments, cachedComments1)

        // 삭제
        val result = commentService.deleteCommentById(comment1.id)
        assertTrue(result)

        // 캐시에서 삭제가 됐는지 확인
        val cachedComments2 = cacheManager.getCache("commentsByPost")?.get(post1.id, List::class.java)
        assertNull(cachedComments2)
    }
}