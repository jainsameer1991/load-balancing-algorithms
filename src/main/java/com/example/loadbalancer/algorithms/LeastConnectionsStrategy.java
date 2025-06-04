package com.example.loadbalancer.algorithms;

import java.util.ArrayList;
import java.util.List;

public class LeastConnectionsStrategy implements LoadBalancingStrategy {
    private final List<String> serverList = new ArrayList<>();
    private final List<Integer> connectionList = new ArrayList<>();

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
        List<Integer> connections = (List<Integer>) requestInfo;
        int minIndex = 0;
        int minConnections = connections.get(0);
        for (int i = 1; i < connections.size(); i++) {
            if (connections.get(i) < minConnections) {
                minConnections = connections.get(i);
                minIndex = i;
            }
        }
        return serverList.get(minIndex);
    }

    @Override
    public void addServer(String server) {
        if (!serverList.contains(server)) {
            serverList.add(server);
            connectionList.add(0); // Default 0 connections
        }
    }

    @Override
    public void removeServer(String server) {
        int idx = serverList.indexOf(server);
        if (idx >= 0) {
            serverList.remove(idx);
            connectionList.remove(idx);
        }
    }
} 