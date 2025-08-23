package com.At.springMockServer.controller;

import com.At.springMockServer.exception.MockNotFoundException;
import com.At.springMockServer.model.MockEndpoint;
import com.At.springMockServer.service.MockEndpointService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MockController {

    private final MockEndpointService mockEndpointService;
    private final ObjectMapper objectMapper;

    public MockController(MockEndpointService mockEndpointService) {
        this.mockEndpointService = mockEndpointService;
        this.objectMapper = new ObjectMapper();
    }

    @RequestMapping("/**")
    public ResponseEntity<?> handleAllRequests(
            HttpServletRequest request,
            @RequestBody(required = false) String body
    ) throws InterruptedException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        List<MockEndpoint> endpoints = mockEndpointService.getMockEndpoints();

        Optional<MockEndpoint> matched = endpoints.stream()
                .filter(ep -> ep.getPath().equals(path))
                .filter(ep -> ep.getMethod().equalsIgnoreCase(method))
                .filter(ep -> {
                    try {
                        // 1. Full JSON body exact match (structural match)
                        if (ep.getExpectedRequestBody() != null) {
                            if (body == null) return false;

                            Map<String, Object> expectedMap = objectMapper.readValue(ep.getExpectedRequestBody(), new TypeReference<>() {});
                            Map<String, Object> actualMap = objectMapper.readValue(body, new TypeReference<>() {});

                            return expectedMap.equals(actualMap);
                        }

                        // 2. Partial key-value matching for expectedBodyContains
                        if (ep.getExpectedBodyContains() != null) {
                            if (body == null) return false;

                            Map<String, Object> actualMap = objectMapper.readValue(body, new TypeReference<>() {});

                            for (Map.Entry<String, String> entry : ep.getExpectedBodyContains().entrySet()) {
                                if (!actualMap.containsKey(entry.getKey())) return false;

                                Object val = actualMap.get(entry.getKey());
                                if (val == null || !val.toString().equals(entry.getValue())) return false;
                            }
                            return true;
                        }

                        // 3. No body expectations defined, match any body
                        if (ep.getExpectedRequestBody() == null && ep.getExpectedBodyContains() == null) {
                            return true;
                        }
                    } catch (Exception e) {
                        // Parsing or matching error, skip this mock
                        return false;
                    }
                    return false;
                })
                .findFirst();

        if (matched.isEmpty()) {
            throw new MockNotFoundException("No mock configured for " + method + " " + path);
        }

        MockEndpoint endpoint = matched.get();

        // Simulate delay if configured
        if (endpoint.getResponseDelayMs() > 0) {
            Thread.sleep(endpoint.getResponseDelayMs());
        }

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        if (endpoint.getResponseHeaders() != null) {
            endpoint.getResponseHeaders().forEach(headers::add);
        }
        headers.add("Content-Type", "application/json");

        return ResponseEntity.status(endpoint.getResponseStatus())
                .headers(headers)
                .body(endpoint.getResponseBody());
    }
}
