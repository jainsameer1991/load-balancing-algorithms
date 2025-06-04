package com.example.loadbalancer.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WeightedRoundRobinStrategy implements LoadBalancingStrategy {
    private final AtomicInteger index = new AtomicInteger(0);
    private final List<String> serverList = new ArrayList<>();
    private final List<Integer> weightList = new ArrayList<>();

    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        // Sync internal list with provided servers
        if (!serverList.equals(servers)) {
            serverList.clear();
            serverList.addAll(servers);
        }
        if (serverList.isEmpty() || requestInfo == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<Integer> weights = (List<Integer>) requestInfo;
        List<String> weightedServers = new ArrayList<>();
        for (int i = 0; i < serverList.size(); i++) {
            for (int w = 0; w < weights.get(i); w++) {
                weightedServers.add(serverList.get(i));
            }
        }
        if (weightedServers.isEmpty()) {
            return null;
        }
        int i = Math.abs(index.getAndIncrement() % weightedServers.size());
        return weightedServers.get(i);
    }

    @Override
    public void addServer(String server) {
        if (!serverList.contains(server)) {
            serverList.add(server);
            weightList.add(1); // Default weight
        }
    }

    @Override
    public void removeServer(String server) {
        int idx = serverList.indexOf(server);
        if (idx >= 0) {
            serverList.remove(idx);
            weightList.remove(idx);
        }
    }
} 