package se.slapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import se.slapi.service.AssignmentTask;
import se.slapi.service.BuslineService;


@Configuration
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Main.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
    @Component
    public class CommandLiner implements CommandLineRunner {

        @Autowired
        private BuslineService buslineService;

        @Override
        public void run(String... args) throws Exception {
            System.out.println("SL API");
            new AssignmentTask().task(buslineService);
        }

    }

}