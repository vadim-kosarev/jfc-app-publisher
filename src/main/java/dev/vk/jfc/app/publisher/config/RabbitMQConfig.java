package dev.vk.jfc.app.publisher.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Data
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.virtual-host}")
    private String vhost;
    @Value("${spring.rabbitmq.images_exchange}")
    private String images_exchange;
    @Value("${spring.rabbitmq.images_routing_key}")
    private String images_routing_key;

}
