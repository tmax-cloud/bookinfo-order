package org.tmaxcloud.sample.msa.book.order;

import javax.persistence.*;

@Entity
@Table(name = "BOOK_ORDER_PAYMENT")
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long paymentId;

    public OrderPayment() {}

    public OrderPayment(Long orderId, Long paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return String.format(
                "OrderPayment[id='%d', orderId='%d' paymentId='%d']",
                id, orderId, paymentId);
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Long getPaymentId() {
        return this.paymentId;
    }

    public void setOrderId(Long id) {
        this.orderId = id;
    }

    public void setPaymentId(Long id) {
        this.paymentId = id;
    }
}
