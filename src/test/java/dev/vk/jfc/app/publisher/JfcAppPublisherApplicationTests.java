package dev.vk.jfc.app.publisher;

import dev.vk.jfc.app.publisher.cmd.CmdProcessor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class JfcAppPublisherApplicationTests {

	@Autowired
	private CmdProcessor cmdProcessor;

	@Test
	void contextLoads() {
	}

	@Test
	@SneakyThrows
	void test_RabbitMqConnect() {

		ApplicationArguments args = Mockito.mock(ApplicationArguments.class);
		Mockito.when(args.getOptionValues("file")).thenReturn(List.of("data/dflt_image.jpg"));
		Mockito.when(args.getOptionValues("command")).thenReturn(List.of("processImage"));

		cmdProcessor.run(args);
	}

}
