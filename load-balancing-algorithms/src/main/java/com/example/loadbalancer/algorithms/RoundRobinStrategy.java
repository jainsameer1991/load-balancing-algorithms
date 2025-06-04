package com.example.loadbalancer.algorithms;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements LoadBalancingStrategy {
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        int i = Math.abs(index.getAndIncrement() % servers.size());
        return servers.get(i);
    }
} 