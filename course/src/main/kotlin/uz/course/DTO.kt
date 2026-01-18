package uz.course

import java.math.BigDecimal

data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class CourseCreateRequest(
    val title: String,
    val price: BigDecimal,
    val description: String
)

data class CourseUpdateRequest(
    val title: String?,
    val price: BigDecimal?,
    val description: String?
)


data class CourseResponse(
    val id: Long,
    val title: String,
    val price: BigDecimal,
    val description: String
) {
    companion object {
        fun toResponse(course: Course) = CourseResponse(
            id = course.id!!,
            title = course.title,
            price = course.price,
            description = course.description
        )
    }
}

data class CourseStatisticsResponse(
    val totalCourses: Long
)
