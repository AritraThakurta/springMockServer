package com.At.springMockServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockEndpoint {

    private String method; // GET, POST, PUT, DELETE
    private String path;   // /api/v1/users
    private int responseStatus = 200;
    private Map<String, String> responseHeaders;
    private String responseBody; // Can be a JSON string or template
    private long responseDelayMs = 0; // delay in milliseconds to simulate latency
    private String expectedRequestBody; // raw string match
    private Map<String, String> expectedRequestHeaders;
    private Map<String,String> expectedBodyContains;
}
