package org.tmaxcloud.sample.msa.book.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.tmaxcloud.sample.msa.book.common.dto.OrderDto;
import org.tmaxcloud.sample.msa.book.common.dto.PaymentDto;
import reactor.core.publisher.Mono;

@Service
public class OrderPaymentService {

    private static final Logger log = LoggerFactory.getLogger(OrderPaymentService.class);

    private final OrderPaymentRepository paymentRepository;
    private final WebClient webClient;

    @Value("${upstream.payment}")
    private String paymentSvcAddr;

    public OrderPaymentService(OrderPaymentRepository paymentRepository, WebClient webClient) {
        this.paymentRepository = paymentRepository;
        this.webClient = webClient;
    }

    public void issuePaymentID(Order order) {
        log.info("issue payment for order: {}", order.getId());
        OrderDto newOrder = new OrderDto().setId(order.getId());

        Mono<PaymentDto> response = webClient.post()
                .uri(paymentSvcAddr + "/api/payments")
                .bodyValue(newOrder)
                .retrieve()
                .bodyToMono(PaymentDto.class);


        response.subscribe(res -> {
            OrderPayment orderPaymentDto = new OrderPayment(res.getOrderId(), res.getId());
            paymentRepository.save(orderPaymentDto);
            log.info("success to save order payment: {}", orderPaymentDto);
        }, e -> {
            log.warn("failed to issue payment id for order: {}", order.getId());
        });
        return;
    }
}
