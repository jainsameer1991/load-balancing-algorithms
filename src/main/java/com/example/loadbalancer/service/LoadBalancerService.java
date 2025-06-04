package com.example.loadbalancer.service;

import com.example.loadbalancer.algorithms.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoadBalancerService {
    private final Map<String, LoadBalancingStrategy> strategies = new HashMap<>();
    private String currentStrategy = "RoundRobin";
    private final List<String> serverPool = new ArrayList<>();

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

    public List<String> getServerPool() {
        return new ArrayList<>(serverPool);
    }

    public void addServer(String server) {
        if (!serverPool.contains(server)) {
            serverPool.add(server);
            strategies.values().forEach(s -> s.addServer(server));
        }
    }

    public void removeServer(String server) {
        serverPool.remove(server);
        strategies.values().forEach(s -> s.removeServer(server));
    }

    public void setServerPool(List<String> servers) {
        serverPool.clear();
        serverPool.addAll(servers);
    }

    public String selectServer(List<String> servers, Object requestInfo) {
        List<String> useServers = (servers == null) ? serverPool : servers;
        return strategies.get(currentStrategy).selectServer(useServers, requestInfo);
    }
} 