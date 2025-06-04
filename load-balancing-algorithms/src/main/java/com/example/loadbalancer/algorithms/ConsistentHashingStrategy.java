package com.example.loadbalancer.algorithms;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashingStrategy implements LoadBalancingStrategy {
    private final SortedMap<Integer, String> ring = new TreeMap<>();
    private boolean initialized = false;

    private void initialize(List<String> servers) {
        ring.clear();
        for (String server : servers) {
            int hash = server.hashCode();
            ring.put(hash, server);
        }
        initialized = true;
    }

    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        if (servers == null || servers.isEmpty() || requestInfo == null) {
            return null;
        }
        if (!initialized || ring.size() != servers.size()) {
            initialize(servers);
        }
        int hash = requestInfo.hashCode();
        SortedMap<Integer, String> tailMap = ring.tailMap(hash);
        int key = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        return ring.get(key);
    }
} 