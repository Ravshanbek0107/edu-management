package uz.payment

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PostMapping
    fun create(@RequestBody request: PaymentCreateRequest): PaymentResponse {
        return paymentService.create(request)
    }


    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): PaymentResponse {
        return paymentService.getOne(id)
    }


    @GetMapping
    fun getAll(): List<PaymentResponse> {
        return paymentService.getAll()
    }

    @GetMapping("/statistics")
    fun statistics(): PaymentStatResponse {
        return paymentService.statistics()
    }
}
