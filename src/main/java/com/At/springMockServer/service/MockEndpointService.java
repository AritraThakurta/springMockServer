package com.At.springMockServer.service;

import com.At.springMockServer.model.MockEndpoint;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Getter
@Service
public class MockEndpointService {

    private List<MockEndpoint> mockEndpoints;

    @PostConstruct
    public void loadMocks() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/mocks.json");
            mockEndpoints = objectMapper.readValue(is, new TypeReference<>() {});
            System.out.println("✅ Loaded " + mockEndpoints.size() + " mock endpoints");
        } catch (Exception e) {
            System.err.println("❌ Failed to load mocks.json: " + e.getMessage());
            throw new RuntimeException("Unable to load mock definitions", e);
        }
    }

}
