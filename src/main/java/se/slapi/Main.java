package se.slapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import se.slapi.repository.busline.BusLineRepository;


@Configuration
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Component
    public class CommandLiner implements CommandLineRunner {

        @Autowired
        private BusLineRepository busLineRepository;

        @Override
        public void run(String... args) throws Exception {
            System.out.println("SL API");
            busLineRepository.getStopsAndLines();
        }

    }

}