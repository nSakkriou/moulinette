package com.nathansakkriou.moulinette.moulinettespringdemo;

import com.nathansakkriou.domain.OnFailBehaviour;
import com.nathansakkriou.domain.ReplayabilityBehaviour;
import com.nathansakkriou.domain.api.MoulinetteApi;
import com.nathansakkriou.domain.persist.Persist;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoulinetteSpringDemoApplication {

    public MoulinetteSpringDemoApplication(Persist persist) {
        this.persist = persist;
    }

    public static void main(String[] args) {
        SpringApplication.run(MoulinetteSpringDemoApplication.class, args);
    }

    private final Persist persist;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
          MoulinetteApi.create()
                  .withPersistance(persist)
                  .with("test", "test", () -> {
                      System.out.println("test");
                  })
                  .withReplayabilityBehaviour(ReplayabilityBehaviour.RUN_ONCE)
                  .withOnFailBehaviour(OnFailBehaviour.STOP_ON_FAIL)
                  .build().run();
        };
    }
}
