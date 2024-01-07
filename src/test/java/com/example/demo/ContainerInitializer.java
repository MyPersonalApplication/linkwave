package com.example.demo;

import java.io.File;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

@Slf4j
public class ContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    private final static DockerComposeContainer<?> COMPOSE_CONTAINER = new DockerComposeContainer<>(
            new File("docker-compose-test.yml"))
            .withLocalCompose(true)
            .waitingFor("linkwave-db-test", Wait.forHealthcheck());

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        COMPOSE_CONTAINER.start();
    }
}
