package com.alexdan.companion.controllers;
import com.alexdan.companion.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping("/")
    public String index(Model model, @AuthenticationPrincipal User profile, @Value("${mode:production}") String mode) {

        if (profile != null) {
            Set<String> roles = new LinkedHashSet<>();
            profile.getAuthorities().forEach( role -> {
                roles.add(role.getAuthority());
            });
            model.addAttribute("roles", roles);
            model.addAttribute("profile", profile);
        }

        model.addAttribute("isProdMode", "production".equals(mode));

        return "index";
    }
}
