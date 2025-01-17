package com.luxusxc.rank_up.web.controllers;

import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.service.WebConfigValidator;
import com.luxusxc.rank_up.service.WebRankUpConfigurer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class MainController {
    private final WebRankUpConfigurer configurer;
    private final WebConfigValidator validator;

    @GetMapping("/")
    private String getConfigForm(Model model) {
        model.addAttribute("webConfig", configurer.exportWebConfig());
        return "config";
    }

    @PostMapping("/")
    private String setConfig(@ModelAttribute("webConfig") WebRankUpConfig config, RedirectAttributes redirectAttr) {
        if (validator.isValid(config)) {
            configurer.importWebConfig(config);
            redirectAttr.addFlashAttribute("saved", true);
        } else {
            redirectAttr.addFlashAttribute("notsaved", true);
        }
        return "redirect:/";
    }
}
