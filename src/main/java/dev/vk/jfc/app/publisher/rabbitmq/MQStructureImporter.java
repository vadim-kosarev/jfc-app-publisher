package dev.vk.jfc.app.publisher.rabbitmq;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MQStructureImporter {

    private final static Logger logger = LoggerFactory.getLogger(MQStructureImporter.class);


    private final Client client;

    @PostConstruct
    public void setup() {
        logger.info("Setup RabbitMQ structure ...");
        logger.info("Setup RabbitMQ structure done");
    }

}
