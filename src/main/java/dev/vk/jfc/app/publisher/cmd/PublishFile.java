package dev.vk.jfc.app.publisher.cmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vk.jfc.app.publisher.config.RabbitMQConfig;
import dev.vk.jfc.app.publisher.rabbitmq.Client;
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
import java.util.Random;

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

            ImageMessage.MessageHeaders msgHeaders = new ImageMessage.MessageHeaders();
            msgHeaders.put(ImageMessage.HeaderKeys.K_BROKER_TIMESTAMP, System.currentTimeMillis());

            Path filePath = Paths.get(file);

            Random rnd = new Random();
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            msgHeaders.put(ImageMessage.HeaderKeys.K_TIMESTAMP, attrs.lastModifiedTime().toMillis());
            msgHeaders.put(ImageMessage.HeaderKeys.K_SOURCE, file);
            msgHeaders.put(ImageMessage.HeaderKeys.K_HOSTNAME, Inet4Address.getLocalHost().getHostName());
            msgHeaders.put(ImageMessage.HeaderKeys.K_LOCALID, rnd.nextInt(100000, 999999));
            msgHeaders.put(ImageMessage.HeaderKeys.K_FRAMENO, rnd.nextInt(0, 60));

            byte[] bytes = Files.readAllBytes(filePath);
            ImageMessage.MessageFile mFile = new ImageMessage.MessageFile("image/jpeg", bytes);

            ImageMessage.Message msg = new ImageMessage.Message(msgHeaders, mFile);
            msg.ensureValid();

            ObjectMapper objectMapper = new ObjectMapper();
            String msgStr = objectMapper.writeValueAsString(msg);

            logger.info("Headers: {}", objectMapper.writeValueAsString(msgHeaders));
            logger.info("Processing file message: {}", msgStr);

            MessageProperties mqProps = new MessageProperties();
            mqProps.setHeaders(msg.getHeaders().copyMap());
            Message mqMessage = MessageBuilder
                    .withBody(msgStr.getBytes())
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
