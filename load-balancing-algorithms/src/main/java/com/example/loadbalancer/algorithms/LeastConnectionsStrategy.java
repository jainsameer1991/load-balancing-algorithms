package com.example.loadbalancer.algorithms;

import java.util.List;

public class LeastConnectionsStrategy implements LoadBalancingStrategy {
    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        if (servers == null || servers.isEmpty() || requestInfo == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<Integer> connections = (List<Integer>) requestInfo;
        int minIndex = 0;
        int minConnections = connections.get(0);
        for (int i = 1; i < connections.size(); i++) {
            if (connections.get(i) < minConnections) {
                minConnections = connections.get(i);
                minIndex = i;
            }
        }
        return servers.get(minIndex);
    }
} 