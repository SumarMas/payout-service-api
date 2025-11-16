package com.platform.payout_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Main application class for the Payout Service.
 */
@SpringBootApplication
@Slf4j
public class PayoutServiceApplication implements CommandLineRunner {
    /** RabbitListenerEndpointRegistry to manage RabbitMQ listeners. */
    private final RabbitListenerEndpointRegistry registry;

    /**
     * Constructor to initialize RabbitListenerEndpointRegistry.
     * @param registryParam RabbitListenerEndpointRegistry instance
     */
    public PayoutServiceApplication(RabbitListenerEndpointRegistry registryParam) {
        this.registry = registryParam;
    }

    /**
     * Main method to run the Spring Boot application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(PayoutServiceApplication.class, args);
    }

    /**
     * Method to start RabbitMQ listeners on application startup.
     * @param args command-line arguments
     * @throws Exception if an error occurs while starting listeners
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting RabbitMQ Payout Service Application");
        registry.start();
    }

}
