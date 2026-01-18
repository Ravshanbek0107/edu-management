package uz.user

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun create(@RequestBody request: UserCreateRequest): UserResponse {
        return userService.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UserUpdateRequest): UserResponse {
        return userService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): BaseMessage {
        userService.delete(id)
        return BaseMessage.OK
    }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): UserResponse {
        return userService.getOne(id)
    }

    @GetMapping
    fun getAll(): List<UserResponse> {
        return userService.getAll()
    }

    @PostMapping("/{id}/increase-balance")
    fun increaseBalance(@PathVariable id: Long, @RequestBody request: BalanceRequest): BalanceResponse{
        return userService.increaseBalance(id, request)
    }

    @PostMapping("/{id}/decrease-balance")
    fun decreaseBalance(@PathVariable id: Long, @RequestBody request: BalanceRequest): BalanceResponse {
        return userService.decreaseBalance(id, request)
    }
    @GetMapping("/{id}/courses")
    fun myCourses(@PathVariable id: Long): List<CourseResponse> {
        return userService.getUserCourses(id)
    }
}