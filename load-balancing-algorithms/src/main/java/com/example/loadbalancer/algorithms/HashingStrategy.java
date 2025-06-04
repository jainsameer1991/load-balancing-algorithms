package com.example.loadbalancer.algorithms;

import java.util.List;

public class HashingStrategy implements LoadBalancingStrategy {
    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        if (servers == null || servers.isEmpty() || requestInfo == null) {
            return null;
        }
        int hash = requestInfo.hashCode();
        int index = Math.abs(hash % servers.size());
        return servers.get(index);
    }
} 