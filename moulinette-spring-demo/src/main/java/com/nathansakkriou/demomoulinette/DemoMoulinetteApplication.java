package com.nathansakkriou.demomoulinette;

import com.nathansakkriou.domain.MoulinetteProcessor;
import com.nathansakkriou.domain.ReplayabilityBehaviour;
import com.nathansakkriou.domain.api.MoulinetteApi;
import com.nathansakkriou.domain.persist.Persist;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoMoulinetteApplication implements CommandLineRunner {

    public DemoMoulinetteApplication(Persist persist) {
        this.persist = persist;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoMoulinetteApplication.class, args);
    }

    private final Persist persist;

    @Bean
    public MoulinetteProcessor processor() {
        return MoulinetteApi.Companion.create()
                .withPersistance(persist)
                .with("test", "me", () -> System.out.println("salut"))
                .withReplayabilityBehaviour(ReplayabilityBehaviour.RUN_ONCE)
                .build();
    }


    @Override
    public void run(String... args) throws Exception {
        processor().run();
    }
}
