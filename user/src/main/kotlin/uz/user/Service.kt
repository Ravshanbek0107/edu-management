package uz.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

interface UserService {
    fun create(request: UserCreateRequest): UserResponse
    fun update(id: Long, request: UserUpdateRequest): UserResponse
    fun delete(id: Long)
    fun getOne(id: Long): UserResponse
    fun getAll(): List<UserResponse>

    fun increaseBalance(userId: Long, request: BalanceRequest): BalanceResponse
    fun decreaseBalance(userId: Long, request: BalanceRequest): BalanceResponse
    fun getUserCourses(userId: Long): List<CourseResponse>
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val courseFeignClient: CourseFeignClient
) : UserService {

    @Transactional
    override fun create(request: UserCreateRequest): UserResponse {

        val user = userRepository.findAllNotDeleted().any{
            it.username == request.username
        }

        if (user) throw UserAlreadyExistsException()



        val newUser = User(
            username = request.username,
            fullname = request.fullname,
            password = request.password,
            balance = request.balance,
        )

        userRepository.save(newUser)
        return UserResponse.toResponse(newUser)
    }

    @Transactional
    override fun update(id: Long, request: UserUpdateRequest): UserResponse {

        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        request.username?.let { newUsername ->
            val exists = userRepository.findAllNotDeleted().any {
                it.username == newUsername && it.id != user.id
            }
            if (exists) throw UsernameAlreadyTakenException()

            user.username = newUsername
        }

        request.fullname?.let { user.fullname = it }

        request.password?.let {
            user.password = it
        }

        userRepository.save(user)
        return UserResponse.toResponse(user)
    }

    @Transactional
    override fun delete(id: Long) {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        userRepository.trash(user.id!!)
    }

    override fun getOne(id: Long): UserResponse {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        return UserResponse.toResponse(user)
    }

    override fun getAll(): List<UserResponse> {
        return userRepository.findAllNotDeleted().map{
            UserResponse.toResponse(it)
        }
    }
    @Transactional
    override fun increaseBalance(userId: Long, request: BalanceRequest): BalanceResponse {

        if (request.amount < BigDecimal.ZERO) throw AmountMustBePositiveException()

        val user = userRepository.findByIdAndDeletedFalse(userId) ?: throw UserNotFoundException()

        user.balance += request.amount

        userRepository.save(user)

        return BalanceResponse(
            userId = user.id!!,
            balance = user.balance
        )
    }

    @Transactional
    override fun decreaseBalance(userId: Long, request: BalanceRequest): BalanceResponse {

        if (request.amount < BigDecimal.ZERO) throw AmountMustBePositiveException()

        val user = userRepository.findByIdAndDeletedFalse(userId) ?: throw UserNotFoundException()

        if (user.balance < request.amount) {
            throw InsufficientBalanceException()
        }

        user.balance -= request.amount

        userRepository.save(user)

        return BalanceResponse(
            userId = user.id!!,
            balance = user.balance
        )
    }

    override fun getUserCourses(userId: Long): List<CourseResponse> {
        return courseFeignClient.getUserCourses(userId)
    }

}