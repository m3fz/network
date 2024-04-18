package com.soc.network.core.controller

import com.soc.network.core.model.dto.PostDto
import com.soc.network.core.model.entity.UserEntity
import com.soc.network.core.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/post")
@SecurityRequirement(name = "basicAuth")
class PostController(
    private val postService: PostService
) {

    @PostMapping("/create")
    @Operation(description = "Создание поста")
    fun createPost(@RequestBody postDto: PostDto,
                   @AuthenticationPrincipal user: UserEntity) :UUID {
        return postService.createPost(postDto, user.uuid)
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Обновление поста")
    fun updatePost(@RequestBody postDto: PostDto,
                   @AuthenticationPrincipal user: UserEntity): UUID {
        return postService.updatePost(postDto, user.uuid)
    }

    @DeleteMapping("/delete/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Удаление поста")
    fun deletePost(@PathVariable uuid: UUID) {
        postService.deletePost(uuid)
    }

    @GetMapping("/get/{uuid}")
    @Operation(description = "Получение поста")
    fun getPost(@PathVariable uuid: UUID): PostDto {
        return postService.getPost(uuid)
    }

    @GetMapping("/feed")
    @Operation(description = "Получение ленты постов друзей")
    fun getFeed(@RequestParam(defaultValue = "10") limit: Int,
                @RequestParam(defaultValue = "0") offset: Int,
                @AuthenticationPrincipal user: UserEntity): List<PostDto> {
        return postService.getFeed(user.uuid, limit, offset)
    }

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(exception: NoSuchElementException): String? {
        return exception.message
    }
}