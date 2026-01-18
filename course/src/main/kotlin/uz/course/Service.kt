package uz.course

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


interface CourseService {

    fun create(request: CourseCreateRequest): CourseResponse
    fun update(id: Long, request: CourseUpdateRequest): CourseResponse
    fun delete(id: Long)
    fun getOne(id: Long): CourseResponse
    fun getAll(): List<CourseResponse>
    // payment SUCCESS bolgandan keyin chaqiriladi
    fun confirmPurchase(courseId: Long, userId: Long)
    fun statistics(): CourseStatisticsResponse
    fun getCoursesByUser(userId: Long): List<CourseResponse>
}

@Service
class CourseServiceImpl(
    private val courseRepository: CourseRepository,
    private val userCourseRepository: UserCourseRepository,
) : CourseService {

    @Transactional
    override fun create(request: CourseCreateRequest): CourseResponse {
        val course = Course(
            title = request.title,
            price = request.price,
            description = request.description
        )
        courseRepository.save(course)
        return CourseResponse.toResponse(course)
    }

    @Transactional
    override fun update(id: Long, request: CourseUpdateRequest): CourseResponse {
        val course = courseRepository.findByIdAndDeletedFalse(id) ?: throw CourseNotFoundException()

        request.title?.let { course.title = it }
        request.price?.let { course.price = it }
        request.description?.let { course.description = it }

        return CourseResponse.toResponse(courseRepository.save(course))
    }

    @Transactional
    override fun delete(id: Long) {
        courseRepository.trash(id) ?: throw CourseNotFoundException()
    }

    override fun getOne(id: Long): CourseResponse {
        val course = courseRepository.findByIdAndDeletedFalse(id) ?: throw CourseNotFoundException()
        return CourseResponse.toResponse(course)
    }

    override fun getAll(): List<CourseResponse> =
        courseRepository.findAllNotDeleted().map { CourseResponse.toResponse(it) }

    @Transactional
    override fun confirmPurchase(courseId: Long, userId: Long) {

        courseRepository.findByIdAndDeletedFalse(courseId) ?: throw CourseNotFoundException()

        val alreadyBought = userCourseRepository.existsByUserIdAndCourseId(userId, courseId)

        if (alreadyBought) throw CourseAlreadyPurchasedException()


        userCourseRepository.save(
            UserCourse(
                userId = userId,
                courseId = courseId
            )
        )
    }

    override fun statistics(): CourseStatisticsResponse {
        val totalCourses = courseRepository.count()
        return CourseStatisticsResponse(
            totalCourses = totalCourses
        )
    }

    override fun getCoursesByUser(userId: Long): List<CourseResponse> {
        val courseIds = userCourseRepository.findAllByUserId(userId)
            .map { it.courseId }

        if (courseIds.isEmpty()) throw CourseNotFoundException()

        return courseRepository.findAllById(courseIds)
            .map { CourseResponse.toResponse(it) }
    }
}

