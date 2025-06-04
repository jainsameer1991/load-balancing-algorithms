package com.example.loadbalancer.service;

import com.example.loadbalancer.algorithms.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoadBalancerService {
    private final Map<String, LoadBalancingStrategy> strategies = new HashMap<>();
    private String currentStrategy = "RoundRobin";

    public LoadBalancerService() {
        strategies.put("RoundRobin", new RoundRobinStrategy());
        strategies.put("WeightedRoundRobin", new WeightedRoundRobinStrategy());
        strategies.put("LeastConnections", new LeastConnectionsStrategy());
        strategies.put("Hashing", new HashingStrategy());
        strategies.put("ConsistentHashing", new ConsistentHashingStrategy());
        strategies.put("MaglevHashing", new MaglevHashingStrategy());
    }

    public void setStrategy(String name) {
        if (strategies.containsKey(name)) {
            currentStrategy = name;
        }
    }

    public String getCurrentStrategy() {
        return currentStrategy;
    }

    public List<String> getAvailableStrategies() {
        return strategies.keySet().stream().toList();
    }

    public String selectServer(List<String> servers, Object requestInfo) {
        return strategies.get(currentStrategy).selectServer(servers, requestInfo);
    }
} 