package dev.vk.jfc.app.publisher.cmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vk.jfc.app.publisher.config.RabbitMQConfig;
import dev.vk.jfc.app.publisher.rabbitmq.Client;
import dev.vk.jfc.jfccommon.Jfc;
import dev.vk.jfc.jfccommon.dto.ImageMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PublishFile implements CmdProcessor.Processor {

    private final static Logger logger = LoggerFactory.getLogger(PublishFile.class);

    private final Client client;

    @Override
    public String getCommand() {
        return "processImage";
    }

    @Override
    public void processFile(String file, ApplicationArguments args) {
        logger.info("Publish File: {}", file);
        try {

            Map<String, Object> msgHeaders = new HashMap<>();
            msgHeaders.put(Jfc.K_BROKER_TIMESTAMP, System.currentTimeMillis());

            Path filePath = Paths.get(file);

            Random rnd = new Random();
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            msgHeaders.put(Jfc.K_TIMESTAMP, attrs.lastModifiedTime().toMillis());
            msgHeaders.put(Jfc.K_SOURCE, file);
            msgHeaders.put(Jfc.K_HOSTNAME, Inet4Address.getLocalHost().getHostName());
            msgHeaders.put(Jfc.K_LOCALID, rnd.nextInt(100000, 999999));
            msgHeaders.put(Jfc.K_FRAMENO, rnd.nextInt(0, 60));
            msgHeaders.put(Jfc.K_UUID, UUID.randomUUID());

            byte[] payload = Files.readAllBytes(filePath);
            logger.info("Headers: {}", msgHeaders);

            MessageProperties mqProps = new MessageProperties();
            mqProps.setHeaders(msgHeaders);
            Message mqMessage = MessageBuilder
                    .withBody(payload)
                    .andProperties(mqProps)
                    .build();

            RabbitMQConfig config = client.getConfig();
            client.getRabbitTemplate().send(
                    config.getImages_exchange(),
                    config.getImages_routing_key(), mqMessage);
            logger.info("Message sent");

        } catch (Exception e) {
            throw new RuntimeException("Error processing command", e);
        }
    }
}
