package org.tmaxcloud.sample.msa.book.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import orgltmaxcloud.sample.msa.book.common.models.Payment;

import java.util.List;
import java.util.Objects;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderRepository repository;
    private final OrderPaymentRepository paymentRepository;
    private final OrderPaymentService orderPaymentService;
    private final KafkaProducer producer;

    public OrderController(OrderRepository repository, OrderPaymentRepository paymentRepository,
                           OrderPaymentService orderPaymentService,
                           KafkaProducer producer) {
        this.repository = repository;
        this.paymentRepository = paymentRepository;
        this.orderPaymentService = orderPaymentService;
        this.producer = producer;
    }

    @GetMapping("/orders")
    public List<Order> all() {
        return repository.findAll();
    }

    @PostMapping("/orders")
    public Order newOrder(@RequestBody Order order) {
        log.info("{} - order\n", order);

        Order savedOrder = repository.save(order);

        log.info("{} - savedOrder\n", savedOrder);

        orderPaymentService.issuePaymentID(savedOrder);

        return order;
    }

    @PostMapping("/orders/{id}")
    public Order replaceOrder(@RequestBody Order newOrder, @PathVariable Long id) {
        return repository.findById(id)
                .map(order -> {
                    order.setType(newOrder.getType());
                    order.setQuantity(newOrder.getQuantity());
                    order.setBookId(newOrder.getBookId());
                    return repository.save(order);
                })
                .orElseGet(() -> {
                    newOrder.setId(id);
                    return repository.save(newOrder);
                });
    }

    @PostMapping("/orders/{id}/process")
    public String processOrder(@RequestBody Payment payment, @PathVariable Long id) {

        // TODO: check payment id
//        OrderPayment orderPayment = paymentRepository.findByOrderId(id)
//                .orElseThrow(() -> new OrderNotFoundException(id));
//
//        log.info("internal order payment info: {}", orderPayment);
//        log.info("request body payment info: {}", payment);
//
//        if (!Objects.equals(payment.getId(), orderPayment.getPaymentId())) {
//            log.warn("internal order payment id({}) does not match with responses {})",
//                    orderPayment.getPaymentId(), payment.getId());
//            return "failed (doesn't match payment id)";
//        }

        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        switch (order.getType()) {
            case PURCHASE:
                producer.sendPurchaseMessage(new BookMessage(order.getBookId(), order.getQuantity()));
                break;
            case SALE:
                producer.sendSaleMessage(new BookMessage(order.getBookId(), order.getQuantity()));
                break;
            case RENT:
                producer.sendRentMessage(new BookMessage(order.getBookId(), order.getQuantity()));
                break;
            default:
                return "failed (unknown order type)";
        }

        return "success";
    }
}