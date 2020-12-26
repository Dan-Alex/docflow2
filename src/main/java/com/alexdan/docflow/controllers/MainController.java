package com.alexdan.docflow.controllers;

import com.alexdan.docflow.data.TaskRepository;
import com.alexdan.docflow.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("user", user);
        model.addAttribute("lastThreeTasks", taskRepository.findLastThreeTasks(user.getId()));
        return "profile";
    }

    @RequestMapping("/usersList")
    public String usersList(){
        return "usersList";
    }

    @RequestMapping("/registration")
    public String registration(Model model){
        model.addAttribute("user2", new User());
        return "registrationUserPage";
    }

}
