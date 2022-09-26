package com.acme.testsupport.containers;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.ServerSocket;

public class RandomServerPortExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            System.setProperty("server.port", String.valueOf(serverSocket.getLocalPort()));
        }
    }
}
