package uz.payment

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "user", url = "\${services.hosts.user}")
interface UserFeignClient {

    @PostMapping("/api/v1/users/{id}/decrease-balance")
    fun decreaseBalance(@PathVariable id: Long, @RequestBody request: BalanceRequest)

    @PostMapping("/api/v1/users/{id}/increase-balance")
    fun increaseBalance(@PathVariable id: Long, @RequestBody request: BalanceRequest)
}

@FeignClient(name = "course", url = "\${services.hosts.course}")
interface CourseFeignClient {

    @GetMapping("/api/v1/courses/{id}")
    fun getCourse(@PathVariable id: Long): CourseResponse

    @PostMapping("/api/v1/courses/{id}/confirm")
    fun confirmPurchase(@PathVariable id: Long, @RequestParam userId: Long)
}



