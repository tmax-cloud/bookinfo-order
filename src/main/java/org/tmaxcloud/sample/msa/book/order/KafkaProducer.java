package org.tmaxcloud.sample.msa.book.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private static final String TOPIC_PURCHASE = "purchase";
    private static final String TOPIC_SALE = "sale";
    private static final String TOPIC_RENT = "rent";

    private final KafkaTemplate<String, BookMessage> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, BookMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPurchaseMessage(BookMessage msg) {
        log.info(String.format("Produce purchase message : %s", msg));
        this.kafkaTemplate.send(TOPIC_PURCHASE, msg);

    }

    public void sendSaleMessage(BookMessage msg) {
        log.info(String.format("Produce sale message : %s", msg));
        this.kafkaTemplate.send(TOPIC_SALE, msg);
    }

    public void sendRentMessage(BookMessage msg) {
        log.info(String.format("Produce rent message : %s", msg));
        this.kafkaTemplate.send(TOPIC_RENT, msg);
    }
}
