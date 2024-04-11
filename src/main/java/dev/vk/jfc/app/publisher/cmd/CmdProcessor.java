package dev.vk.jfc.app.publisher.cmd;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CmdProcessor implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(CmdProcessor.class);
    private final List<Processor> proc;
    private List<String> files = new ArrayList<>();
    private List<String> commands = new ArrayList<>();

    private ApplicationArguments args;

    private void setupArgs(ApplicationArguments args) {
        this.args = args;
        files = args.getOptionValues("file");
        commands = args.getOptionValues("command");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Args: {}", args);
        setupArgs(args);
        for (String aFile : files) {
            processFile(aFile);
        }
    }

    private void processFile(String aFile) {
        logger.info("!!! Processing {}", aFile);
        for (String command : commands) {
            logger.info("+++ {}: {}", command, aFile);
            processCommandOnFile(command, aFile);
        }
    }

    private void processCommandOnFile(String command, String aFile) {
        if (null == proc) return;
        for (Processor p: proc) {
            if (command.equals(p.getCommand())) {
                p.processFile(aFile, args);
            }
        }
    }

    public static interface Processor {

        String getCommand();

        void processFile(String file, ApplicationArguments args) throws RuntimeException ;
    }


}
