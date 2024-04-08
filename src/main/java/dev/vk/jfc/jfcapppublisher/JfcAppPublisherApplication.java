package dev.vk.jfc.jfcapppublisher;

import dev.vk.jfc.jfccommon.dto.TestMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JfcAppPublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(JfcAppPublisherApplication.class, args);
		TestMessage obj = new TestMessage();
	}

}
