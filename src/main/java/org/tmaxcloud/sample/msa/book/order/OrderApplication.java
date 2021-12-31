package org.tmaxcloud.sample.msa.book.order;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) { return webClientBuilder.build();}
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
