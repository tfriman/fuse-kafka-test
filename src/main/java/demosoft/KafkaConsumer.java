package demosoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A Camel Application
 */
@SpringBootApplication
public class KafkaConsumer {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) {
        SpringApplication.run(KafkaConsumer.class, args);
    }

}
