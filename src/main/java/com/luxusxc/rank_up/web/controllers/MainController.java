package com.luxusxc.rank_up.web.controllers;

import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.service.RankUpConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    private final RankUpConfigurer configurer;

    public MainController(RankUpConfigurer configurer) {
        this.configurer = configurer;
    }

    @GetMapping("/")
    private String getConfigForm(Model model) {
        model.addAttribute("webConfig", configurer.exportConfig());
        return "config";
    }

    @PostMapping("/")
    private String setConfig(@ModelAttribute("webConfig") WebRankUpConfig config) {
        configurer.importConfig(config);
        return "redirect:/";
    }
}
