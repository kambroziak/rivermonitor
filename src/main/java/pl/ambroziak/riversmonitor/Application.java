package pl.ambroziak.riversmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Created by krzysztof on 30.04.16.
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class Application {

    @Bean
    public RestTemplate getRestTemplate() {

        return new RestTemplate();
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }


}
