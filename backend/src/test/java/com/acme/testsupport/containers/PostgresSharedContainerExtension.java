package com.acme.testsupport.containers;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresSharedContainerExtension implements BeforeAllCallback {
    private static final String IMAGE_VERSION = "postgres:14.5";
    private static PostgreSQLContainer<?> container;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (container == null) {
            container = new PostgreSQLContainer<>(IMAGE_VERSION);
            container.withReuse(true).start();
            System.setProperty("spring.datasource.url", container.getJdbcUrl());
            System.setProperty("spring.datasource.username", container.getUsername());
            System.setProperty("spring.datasource.password", container.getPassword());

        }
    }


}
