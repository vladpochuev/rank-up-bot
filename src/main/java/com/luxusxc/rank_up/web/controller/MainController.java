package com.luxusxc.rank_up.web.controller;

import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.common.service.validation.WebConfigValidator;
import com.luxusxc.rank_up.web.service.WebConfigHandler;
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
    private final WebConfigHandler configurer;
    private final WebConfigValidator validator;

    @GetMapping("/")
    private String getConfigForm(Model model) {
        model.addAttribute("webConfig", configurer.exportWebConfig());
        return "config";
    }

    @PostMapping("/")
    private String setConfig(@ModelAttribute("webConfig") WebConfig config, RedirectAttributes redirectAttr) {
        if (validator.isValid(config)) {
            configurer.importWebConfig(config);
            redirectAttr.addFlashAttribute("saved", true);
        } else {
            redirectAttr.addFlashAttribute("notsaved", true);
        }
        return "redirect:";
    }
}
