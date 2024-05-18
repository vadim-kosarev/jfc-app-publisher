package dev.vk.jfc.app.publisher.cmd;

import dev.vk.jfc.app.publisher.config.RabbitMQConfig;
import dev.vk.jfc.app.publisher.rabbitmq.Client;
import dev.vk.jfc.jfccommon.Jfc;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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

    private final DataSender dataSender;

    @Override
    public String getCommand() {
        return "processImage";
    }



    @SneakyThrows
    @Override
    public void processFile(String file, ApplicationArguments args) {
        Path filePath = Paths.get(file);
        byte[] payload = Files.readAllBytes(filePath);
        long timestamp = Files.readAttributes(filePath, BasicFileAttributes.class)
                .lastModifiedTime().toMillis();
        dataSender.sendFile(payload, timestamp, file, 0);
    }
}
