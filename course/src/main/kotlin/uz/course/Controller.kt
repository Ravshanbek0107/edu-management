package uz.course

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/course")
class CourseController(
    private val courseService: CourseService
) {

    @PostMapping
    fun create(@RequestBody request: CourseCreateRequest) =
        courseService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: CourseUpdateRequest) =
        courseService.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        courseService.delete(id)
    }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long) =
        courseService.getOne(id)

    @GetMapping
    fun getAll() = courseService.getAll()

    //bu endpoint faqat Payment service tomonidan chaqiriladi
    @PostMapping("/confirm/{id}")
    fun confirmPurchase(@PathVariable id: Long, @RequestParam userId: Long){
        courseService.confirmPurchase(id, userId)
    }

    @GetMapping("/statistics")
    fun statistics() = courseService.statistics()

    @GetMapping("/user/{userId}")
    fun getUserCourses(@PathVariable userId: Long): List<CourseResponse> {
        return courseService.getCoursesByUser(userId)
    }
}