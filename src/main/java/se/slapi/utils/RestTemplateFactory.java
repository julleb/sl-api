package se.slapi.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateFactory {

    private static int connectionTimeOutInMs = 10 * 1000;
    private static int readTimeOutInMs = 10 * 1000;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeOutInMs);
        factory.setReadTimeout(readTimeOutInMs);
        return new RestTemplate(factory);
    }
}
