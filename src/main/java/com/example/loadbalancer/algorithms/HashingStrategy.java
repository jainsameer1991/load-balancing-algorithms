package com.example.loadbalancer.algorithms;

import java.util.ArrayList;
import java.util.List;

public class HashingStrategy implements LoadBalancingStrategy {
    private final List<String> serverList = new ArrayList<>();

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
        int hash = requestInfo.hashCode();
        int index = Math.abs(hash % serverList.size());
        return serverList.get(index);
    }

    @Override
    public void addServer(String server) {
        if (!serverList.contains(server)) {
            serverList.add(server);
        }
    }

    @Override
    public void removeServer(String server) {
        serverList.remove(server);
    }
} 