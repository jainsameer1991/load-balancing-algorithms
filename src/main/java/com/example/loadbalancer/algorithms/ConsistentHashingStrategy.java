package com.example.loadbalancer.algorithms;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashingStrategy implements LoadBalancingStrategy {
    // Number of virtual nodes per physical server (tune for your use case)
    private static final int VIRTUAL_NODES = 100;
    private final SortedMap<Integer, String> ring = new TreeMap<>();
    private boolean initialized = false;
    private int lastServerCount = 0;

    // Initialize the hash ring with virtual nodes for each physical server
    private void initialize(List<String> servers) {
        ring.clear();
        for (String server : servers) {
            // For each physical server, create VIRTUAL_NODES virtual nodes
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                // Each virtual node is identified by server#replicaIndex
                String virtualNodeId = server + "#" + i;
                int hash = virtualNodeId.hashCode();
                ring.put(hash, server); // Map virtual node hash to physical server
            }
        }
        initialized = true;
        lastServerCount = servers.size();
    }

    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        // Return null if no servers or request info is provided
        if (servers == null || servers.isEmpty() || requestInfo == null) {
            return null;
        }
        // Re-initialize the hash ring if not initialized or if the server list has changed
        if (!initialized || lastServerCount != servers.size()) {
            initialize(servers);
        }
        // Hash the request info (e.g., client IP or key) to get a position on the ring
        int hash = requestInfo.hashCode();
        // Use ceilingKey to find the first virtual node whose hash is >= the request's hash
        // This is the core of consistent hashing: each request is mapped to the next virtual node clockwise
        // If there is no such virtual node, wrap around to the first one
        Integer key = ((TreeMap<Integer, String>) ring).ceilingKey(hash);
        if (key == null) {
            key = ring.firstKey(); // wrap around
        }
        // Return the physical server mapped to the selected virtual node
        return ring.get(key);
    }

    @Override
    public void addServer(String server) {
        // To rebalance, add the server and re-initialize the ring
        // (In a real system, you would keep a master list of servers)
        initialized = false;
    }

    @Override
    public void removeServer(String server) {
        // To rebalance, remove the server and re-initialize the ring
        initialized = false;
    }
} 