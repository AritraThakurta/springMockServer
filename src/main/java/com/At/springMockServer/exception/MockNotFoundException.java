package com.At.springMockServer.exception;

public class MockNotFoundException extends RuntimeException{

    public MockNotFoundException(String message) {
        super(message);
    }
}
