package dev.vk.jfc.jfcapppublisher.cmd;

import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.help.Help;
import dev.vk.jfc.jfccommon.dto.ImageMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Cli(name = "cmd",
        commands = {
                PublisherCommands.HelloCmd.class,
                PublisherCommands.ByeCmd.class,
                PublisherCommands.PublishCmd.class,
                Help.class},
        defaultCommand = Help.class)
public class PublisherCommands implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(PublisherCommands.class);

    @Override
    public void run(String... args) throws Exception {
        com.github.rvesse.airline.Cli<Runnable> cli = new com.github.rvesse.airline.Cli<Runnable>(PublisherCommands.class);
        Runnable c = cli.parse(args);
        c.run();
    }

    /**
     * ----------------------------------------------------------------------------------------------------------------
     * <p>
     * ----------------------------------------------------------------------------------------------------------------
     */
    public static abstract class Cmd implements Runnable {

        @Option(name = {"-f", "--file"}, description = "File to process")
        protected String file;

        @Inject
        protected HelpOption<Cmd> help;
    }

    /**
     * ----------------------------------------------------------------------------------------------------------------
     * <p>
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Command(name = "hello")
    public static class HelloCmd extends Cmd {

        @Override
        public void run() {
            if (help.showHelpIfRequested()) return;
            logger.info("HELLO processing  {} : ..., {}", file, this);
        }
    }

    /**
     * ----------------------------------------------------------------------------------------------------------------
     * <p>
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Command(name = "bye")
    public static class ByeCmd extends Cmd {

        @Override
        public void run() {
            if (help.showHelpIfRequested()) return;
            logger.info("BYE: ... {}", file);
        }
    }

    /**
     * ----------------------------------------------------------------------------------------------------------------
     * <p>
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Command(name = "publish-message", description = "Loads a file from --file parameter and sends it to MQ")
    public static class PublishCmd extends Cmd {
        @Override
        public void run() {
            if (help.showHelpIfRequested()) return;
            ImageMessage msg = ImageMessage.builder().build();
            /*
             * - Load jpeg file
             * - Setup message headers
             * - Setup message payload
             * - Publish message to RabbitMQ broker
             * */
        }
    }
}
