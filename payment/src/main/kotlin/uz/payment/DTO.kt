package uz.payment

import java.math.BigDecimal

data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class PaymentCreateRequest(
    val userId: Long,
    val courseId: Long
)


data class PaymentResponse(
    val id: Long,
    val userId: Long,
    val courseId: Long,
    val amount: BigDecimal,
    val status: PaymentStatus
) {
    companion object {
        fun toResponse(payment: Payment) = PaymentResponse(
            id = payment.id!!,
            userId = payment.userId,
            courseId = payment.courseId,
            amount = payment.amount,
            status = payment.status
        )
    }
}

data class CourseResponse(
    val id: Long,
    val price: BigDecimal
)
data class BalanceRequest(
    val userId: Long,
    val amount: BigDecimal
)

data class PaymentStatResponse(
    val totalPayments: Long,
    val successPayments: Long,
    val failedPayments: Long,
    val totalAmount: BigDecimal
)