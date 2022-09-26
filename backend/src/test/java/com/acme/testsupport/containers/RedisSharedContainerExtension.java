package com.acme.testsupport.containers;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;

public class RedisSharedContainerExtension implements BeforeAllCallback {
    private static final String IMAGE_VERSION = "redis:7.0.5";
    private static GenericContainer<?> container;

    public RedisSharedContainerExtension() {
        if (container == null) {
            container = new GenericContainer<>(IMAGE_VERSION);
            container.withExposedPorts(6379).withReuse(true).start();
            System.setProperty("spring.redis.host", "localhost");
            System.setProperty("spring.redis.port", String.valueOf(container.getMappedPort(6379)));
        }
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {

    }

}
