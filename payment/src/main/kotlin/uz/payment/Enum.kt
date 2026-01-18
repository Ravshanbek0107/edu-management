package uz.payment

enum class ErrorCode(val code: Int) {
    PAYMENT_NOT_FOUND(300)


}

enum class PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED
}

