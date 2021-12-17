package org.tmaxcloud.sample.msa.book.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tmaxcloud.sample.msa.book.common.dto.OrderDto;
import org.tmaxcloud.sample.msa.book.common.dto.PaymentDto;

@Service
public class OrderPaymentService {

    private static final Logger log = LoggerFactory.getLogger(OrderPaymentService.class);

    private final OrderPaymentRepository paymentRepository;
    private final RestTemplate restTemplate;

    @Value("${upstream.payment}")
    private String paymentSvcAddr;

    public OrderPaymentService(OrderPaymentRepository paymentRepository, RestTemplate restTemplate) {
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
    }

    @Async
    public void issuePaymentID(Order order) {
        log.info("issue payment for order: {}", order.getId());

        ResponseEntity<PaymentDto> response = restTemplate.postForEntity(
                paymentSvcAddr + "/api/payments", new OrderDto().setId(order.getId()), PaymentDto.class);
        if (HttpStatus.OK != response.getStatusCode()) {
            log.warn("failed to issue payment id for order: {}", order.getId());
            return;
        }

        PaymentDto payment = response.getBody();
        OrderPayment orderPaymentDto = new OrderPayment(payment.getOrderId(), payment.getId());
        paymentRepository.save(orderPaymentDto);

        log.info("success to save order payment: {}", orderPaymentDto);
    }
}
