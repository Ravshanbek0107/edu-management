package uz.user

import java.math.BigDecimal

data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class UserCreateRequest(
    val username: String,
    val fullname: String,
    val password: String,
    val balance: BigDecimal
)

data class UserUpdateRequest(
    val username: String?,
    val fullname: String?,
    val password: String?
)


data class UserResponse(
    val id: Long,
    val username: String,
    val fullname: String,
    val balance: BigDecimal,
) {
    companion object {
        fun toResponse(user: User) = UserResponse(
            id = user.id!!,
            username = user.username,
            fullname = user.fullname,
            balance = user.balance
        )
    }
}

data class BalanceRequest(
    val amount: BigDecimal
)

data class BalanceResponse(
    val userId: Long,
    val balance: BigDecimal
)

data class CourseResponse(
    val id: Long,
    val title: String,
    val price: BigDecimal,
    val description: String
)

