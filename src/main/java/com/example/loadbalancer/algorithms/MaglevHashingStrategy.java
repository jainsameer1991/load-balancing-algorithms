package com.example.loadbalancer.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MaglevHashingStrategy implements LoadBalancingStrategy {
    private static final int LOOKUP_TABLE_SIZE = 613; // A prime number for better distribution
    private List<String> servers = new ArrayList<>();
    private int[] lookupTable;
    private boolean initialized = false;

    private int hash(String s) {
        return Math.abs(Objects.hashCode(s));
    }

    private void initialize(List<String> servers) {
        // Maglev does not use explicit virtual nodes like classic consistent hashing.
        // Instead, it builds a large lookup table (of size LOOKUP_TABLE_SIZE),
        // and each physical server is mapped to many slots in this table via a unique permutation.
        // This achieves the effect of virtual nodes (even distribution, minimal disruption)
        // but with a different, more efficient mechanism.
        this.servers = new ArrayList<>(servers);
        int n = servers.size();
        int[] offset = new int[n]; // Each server's starting point in the table
        int[] skip = new int[n];   // Each server's step size (ensures unique permutation)
        lookupTable = new int[LOOKUP_TABLE_SIZE];
        for (int i = 0; i < LOOKUP_TABLE_SIZE; i++) lookupTable[i] = -1;
        // Compute offset and skip for each server
        for (int i = 0; i < n; i++) {
            offset[i] = hash(servers.get(i)) % LOOKUP_TABLE_SIZE;
            skip[i] = (hash(servers.get(i) + "_skip") % (LOOKUP_TABLE_SIZE - 1)) + 1;
        }
        int filled = 0;
        int[] next = new int[n]; // Tracks how many slots have been tried for each server
        // Fill the lookup table so that each slot points to a server, distributing as evenly as possible
        while (filled < LOOKUP_TABLE_SIZE) {
            for (int i = 0; i < n; i++) {
                // Compute the next candidate slot for server i using its permutation
                int c = (offset[i] + next[i] * skip[i]) % LOOKUP_TABLE_SIZE;
                // If the slot is already taken, try the next one in the permutation
                while (lookupTable[c] != -1) {
                    next[i]++;
                    c = (offset[i] + next[i] * skip[i]) % LOOKUP_TABLE_SIZE;
                }
                // Assign this slot to server i
                lookupTable[c] = i;
                next[i]++;
                filled++;
                if (filled >= LOOKUP_TABLE_SIZE) break;
            }
        }
        // After this process, each server is mapped to many slots in the table (like virtual nodes),
        // but the mapping is determined by the permutation logic, not by explicit node creation.
        initialized = true;
    }

    @Override
    public String selectServer(List<String> servers, Object requestInfo) {
        if (servers == null || servers.isEmpty() || requestInfo == null) {
            return null;
        }
        if (!initialized || !this.servers.equals(servers)) {
            initialize(servers);
        }
        int hash = hash(requestInfo.toString()) % LOOKUP_TABLE_SIZE;
        int serverIndex = lookupTable[hash];
        return servers.get(serverIndex);
    }

    @Override
    public void addServer(String server) {
        // To rebalance, add the server and re-initialize the lookup table
        initialized = false;
    }

    @Override
    public void removeServer(String server) {
        // To rebalance, remove the server and re-initialize the lookup table
        initialized = false;
    }
} 