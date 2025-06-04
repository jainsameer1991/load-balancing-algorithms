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
        return "index";
    }

    @PostMapping("/set-strategy")
    public String setStrategy(@RequestParam String strategy) {
        loadBalancerService.setStrategy(strategy);
        return "redirect:/";
    }

    @PostMapping("/select-server")
    public String selectServer(@RequestParam String servers,
                               @RequestParam(required = false) String weights,
                               @RequestParam(required = false) String connections,
                               @RequestParam(required = false) String key,
                               Model model) {
        List<String> serverList = Arrays.stream(servers.split(",")).map(String::trim).toList();
        Object requestInfo = null;
        String strategy = loadBalancerService.getCurrentStrategy();
        if ("WeightedRoundRobin".equals(strategy) && weights != null) {
            requestInfo = Arrays.stream(weights.split(",")).map(String::trim).map(Integer::parseInt).toList();
        } else if ("LeastConnections".equals(strategy) && connections != null) {
            requestInfo = Arrays.stream(connections.split(",")).map(String::trim).map(Integer::parseInt).toList();
        } else if (("Hashing".equals(strategy) || "ConsistentHashing".equals(strategy) || "MaglevHashing".equals(strategy)) && key != null) {
            requestInfo = key;
        }
        String selected = loadBalancerService.selectServer(serverList, requestInfo);
        model.addAttribute("strategies", loadBalancerService.getAvailableStrategies());
        model.addAttribute("currentStrategy", strategy);
        model.addAttribute("selectedServer", selected);
        return "index";
    }
} 