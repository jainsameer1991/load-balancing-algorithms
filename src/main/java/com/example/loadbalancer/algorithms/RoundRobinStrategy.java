package com.example.loadbalancer.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements LoadBalancingStrategy {
    private final AtomicInteger index = new AtomicInteger(0);
    private final List<String> serverList = new ArrayList<>();

    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        // Sync internal list with provided servers
        if (!serverList.equals(servers)) {
            serverList.clear();
            serverList.addAll(servers);
        }
        if (serverList.isEmpty()) {
            return null;
        }
        int i = Math.abs(index.getAndIncrement() % serverList.size());
        return serverList.get(i);
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