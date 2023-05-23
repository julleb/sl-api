package se.slapi.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateFactory {

    @Bean
    public RestTemplate restTemplate() {
        //TODO set http timeout and so on
        return new RestTemplate();
    }
}
