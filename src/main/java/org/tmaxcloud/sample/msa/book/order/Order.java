package org.tmaxcloud.sample.msa.book.order;

import javax.persistence.*;

@Entity
@Table(name = "BOOK_ORDER")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private OrderType type;

    private Long bookId;

    private int quantity;

    public Order(){}

    public Order(OrderType type, Long bookId, int quantity) {
        this.type = type;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(
                "Order[id='%d', type='%s' bookId='%s', quantity='%s']",
                id, type, bookId, quantity);
    }

    public Long getId() {
        return this.id;
    }

    public OrderType getType() {
        return this.type;
    }

    public Long getBookId() {
        return this.bookId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setType(OrderType type) {
        this.type = type;
    }
}
