package spring.ecommerce.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.ecommerce.project.dto.PaymentRequest;
import spring.ecommerce.project.exception.ApiException;
import spring.ecommerce.project.model.*;
import spring.ecommerce.project.repository.PaymentRepository;
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    public Payment processPayment(UserEntity user, PaymentRequest request) {
        OrderEntity order = orderService.getOrderById(request.orderId());

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ApiException("Cannot pay for another user's order");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.paymentMethod());

        if (request.paymentMethod() == PaymentMethod.COD) {
            payment.setStatus(PaymentStatus.PENDING);
        } else {
            payment.setStatus(PaymentStatus.SUCCESS);
        }

        return paymentRepository.save(payment);
    }

    public Payment checkPaymentStatus(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ApiException("Payment not found"));
    }

    public void markPaymentSuccessForOrder(Long orderId) {
        paymentRepository.findByOrderId(orderId).ifPresent(payment -> {
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
        });
    }
}

