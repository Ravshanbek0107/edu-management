package uz.payment

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface PaymentService {
    fun create(request: PaymentCreateRequest): PaymentResponse
    fun getOne(id: Long): PaymentResponse
    fun getAll(): List<PaymentResponse>
    fun statistics(): PaymentStatResponse
}


@Service
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository,
    private val userFeignClient: UserFeignClient,
    private val courseFeignClient: CourseFeignClient,
) : PaymentService {


    override fun create(request: PaymentCreateRequest): PaymentResponse {

        val course = courseFeignClient.getCourse(request.courseId)
        val price = course.price

        val payment = paymentRepository.save(
            Payment(
                userId = request.userId,
                courseId = request.courseId,
                amount = price,
                status = PaymentStatus.PENDING
            )
        )

        try {
            userFeignClient.decreaseBalance(
                request.userId,
                BalanceRequest(price)
            )

            courseFeignClient.confirmPurchase(
                request.courseId,
                request.userId
            )

            payment.status = PaymentStatus.SUCCESS
            paymentRepository.save(payment)

        } catch (ex: Exception) {
            //balance qaytadi failed bolsa
            userFeignClient.increaseBalance(
                request.userId,
                BalanceRequest(price)
            )

            payment.status = PaymentStatus.FAILED
            paymentRepository.save(payment)
            throw ex
        }

        return PaymentResponse(
            id = payment.id!!,
            userId = payment.userId,
            courseId = payment.courseId,
            amount = payment.amount,
            status = payment.status
        )
    }

    override fun getOne(id: Long): PaymentResponse {
        val payment = paymentRepository.findByIdAndDeletedFalse(id) ?: throw PaymentNotFoundException()
        return PaymentResponse.toResponse(payment)
    }

    override fun getAll(): List<PaymentResponse> =
        paymentRepository.findAllNotDeleted()
            .map { PaymentResponse.toResponse(it) }

    override fun statistics(): PaymentStatResponse {
        val success = paymentRepository.findAllByStatus(PaymentStatus.SUCCESS)
        val failed = paymentRepository.findAllByStatus(PaymentStatus.FAILED)

        return PaymentStatResponse(
            totalPayments = paymentRepository.count(),
            successPayments = success.size.toLong(),
            failedPayments = failed.size.toLong(),
            totalAmount = success.sumOf { it.amount }
        )
    }
}

