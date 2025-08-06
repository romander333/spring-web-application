package com.kozak.mybookshop.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String IMAGE_NAME = "mysql:8.0";

    private static CustomMySqlContainer mySqlContainer;

    public CustomMySqlContainer() {
        super(IMAGE_NAME);
    }

    public static synchronized CustomMySqlContainer getInstance() {
        if (mySqlContainer == null) {
            mySqlContainer = new CustomMySqlContainer();
        }
        return mySqlContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", getJdbcUrl());
        System.setProperty("TEST_DB_USER", getUsername());
        System.setProperty("TEST_DB_PASSWORD", getPassword());
    }

    @Override
    public void stop() {
    }
}
