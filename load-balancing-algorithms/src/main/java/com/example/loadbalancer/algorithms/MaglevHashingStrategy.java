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
        this.servers = new ArrayList<>(servers);
        int n = servers.size();
        int[] offset = new int[n];
        int[] skip = new int[n];
        lookupTable = new int[LOOKUP_TABLE_SIZE];
        for (int i = 0; i < LOOKUP_TABLE_SIZE; i++) lookupTable[i] = -1;
        for (int i = 0; i < n; i++) {
            offset[i] = hash(servers.get(i)) % LOOKUP_TABLE_SIZE;
            skip[i] = (hash(servers.get(i) + "_skip") % (LOOKUP_TABLE_SIZE - 1)) + 1;
        }
        int filled = 0;
        int[] next = new int[n];
        while (filled < LOOKUP_TABLE_SIZE) {
            for (int i = 0; i < n; i++) {
                int c = (offset[i] + next[i] * skip[i]) % LOOKUP_TABLE_SIZE;
                while (lookupTable[c] != -1) {
                    next[i]++;
                    c = (offset[i] + next[i] * skip[i]) % LOOKUP_TABLE_SIZE;
                }
                lookupTable[c] = i;
                next[i]++;
                filled++;
                if (filled >= LOOKUP_TABLE_SIZE) break;
            }
        }
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
} 