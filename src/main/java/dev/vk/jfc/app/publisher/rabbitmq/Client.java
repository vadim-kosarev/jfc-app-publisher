package dev.vk.jfc.app.publisher.rabbitmq;

import dev.vk.jfc.app.publisher.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Client {

    @Getter
    private final RabbitMQConfig config;

    @Getter
    private final RabbitTemplate rabbitTemplate;


}
