package com.example.loadbalancer.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WeightedRoundRobinStrategy implements LoadBalancingStrategy {
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        if (servers == null || servers.isEmpty() || requestInfo == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<Integer> weights = (List<Integer>) requestInfo;
        List<String> weightedServers = new ArrayList<>();
        for (int i = 0; i < servers.size(); i++) {
            for (int w = 0; w < weights.get(i); w++) {
                weightedServers.add(servers.get(i));
            }
        }
        if (weightedServers.isEmpty()) {
            return null;
        }
        int i = Math.abs(index.getAndIncrement() % weightedServers.size());
        return weightedServers.get(i);
    }
} 