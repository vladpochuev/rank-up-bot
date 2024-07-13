package com.luxusxc.rank_up.web.controllers;

import com.luxusxc.rank_up.model.WebRankUpConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @GetMapping("/")
    private String getConfigForm(Model model) {
        model.addAttribute("webConfig", new WebRankUpConfig());
        return "config";
    }

    @PostMapping("/")
    private String setConfig(@ModelAttribute("webConfig") WebRankUpConfig config) {
        System.out.println(config);
        return "config";
    }
}
