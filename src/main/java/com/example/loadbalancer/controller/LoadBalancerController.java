package com.example.loadbalancer.controller;

import com.example.loadbalancer.service.LoadBalancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class LoadBalancerController {
    @Autowired
    private LoadBalancerService loadBalancerService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("strategies", loadBalancerService.getAvailableStrategies());
        model.addAttribute("currentStrategy", loadBalancerService.getCurrentStrategy());
        model.addAttribute("servers", loadBalancerService.getServerPool());
        return "index";
    }

    @PostMapping("/set-strategy")
    public String setStrategy(@RequestParam String strategy) {
        loadBalancerService.setStrategy(strategy);
        return "redirect:/";
    }

    @PostMapping("/add-server")
    public String addServer(@RequestParam String server) {
        loadBalancerService.addServer(server);
        return "redirect:/";
    }

    @PostMapping("/remove-server")
    public String removeServer(@RequestParam String server) {
        loadBalancerService.removeServer(server);
        return "redirect:/";
    }

    @PostMapping("/select-server")
    public String selectServer(@RequestParam(required = false) String weights,
                               @RequestParam(required = false) String connections,
                               @RequestParam(required = false) String key,
                               Model model) {
        List<String> serverList = loadBalancerService.getServerPool();
        Object requestInfo = null;
        String strategy = loadBalancerService.getCurrentStrategy();
        List<Integer> weightList = null;
        List<Integer> connectionList = null;
        Integer hashPosition = null;
        if ("WeightedRoundRobin".equals(strategy) && weights != null) {
            weightList = Arrays.stream(weights.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
            requestInfo = weightList;
        } else if ("LeastConnections".equals(strategy) && connections != null) {
            connectionList = Arrays.stream(connections.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
            requestInfo = connectionList;
        } else if (("Hashing".equals(strategy) || "ConsistentHashing".equals(strategy) || "MaglevHashing".equals(strategy)) && key != null) {
            requestInfo = key;
            // Compute hash position for visualization (0-359 degrees)
            int hash = key.hashCode();
            int pos = Math.abs(hash % 360);
            hashPosition = pos;
        }
        String selected = loadBalancerService.selectServer(serverList, requestInfo);
        model.addAttribute("strategies", loadBalancerService.getAvailableStrategies());
        model.addAttribute("currentStrategy", strategy);
        model.addAttribute("servers", serverList);
        model.addAttribute("selectedServer", selected);
        model.addAttribute("weights", weightList);
        model.addAttribute("connections", connectionList);
        model.addAttribute("hashPosition", hashPosition);
        model.addAttribute("requestKey", key);
        // Step-by-step explanation
        String explanation = null;
        if (selected != null && key != null && ("ConsistentHashing".equals(strategy) || "MaglevHashing".equals(strategy) || "Hashing".equals(strategy))) {
            explanation = String.format("Key '%s' hashes to position %d°, which maps to server '%s'", key, hashPosition, selected);
        }
        model.addAttribute("explanation", explanation);
        return "index";
    }
} 