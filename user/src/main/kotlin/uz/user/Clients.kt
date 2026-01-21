package uz.user

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "course", url = "\${services.hosts.course}")
interface CourseFeignClient {

    @GetMapping("/api/v1/course/user/{userId}")
    fun getUserCourses(@PathVariable userId: Long): List<CourseResponse>
}
