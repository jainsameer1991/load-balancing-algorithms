package com.example.loadbalancer.algorithms;

import java.util.List;

public interface LoadBalancingStrategy {
    String selectServer(List<String> servers, Object requestInfo);
} 