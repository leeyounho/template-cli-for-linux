package com.younho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "my-cli", mixinStandardHelpOptions = true, version = "1.0",
        description = "간단한 쿼리를 수행하는 CLI 프로그램")
public class MainCommand implements Callable<Integer> {
    private Logger logger = LoggerFactory.getLogger(MainCommand.class);

    @Option(names = {"--id"}, description = "id")
    private Long id;

    @Option(names = {"-p", "--param"}, description = "parameter")
    private String param;

    @Override
    public Integer call() {
        logger.info("CLI initialized. id={} param={}", id, param);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        try {
            MyService myService = context.getBean(MyService.class);
            myService.processData(1L, param);

            logger.info("Finished");
            return 0;
        } catch (Exception e) {
            logger.error("Error occurred: {}", e.getMessage(), e);
            return 1;
        } finally {
            context.close();
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainCommand()).execute(args);
        System.exit(exitCode);
    }
}
