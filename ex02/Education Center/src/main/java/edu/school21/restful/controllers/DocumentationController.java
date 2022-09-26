package edu.school21.restful.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs/publish")
public class DocumentationController {
    @GetMapping
    public String publish() {
        return "redirect:/docs/publish.html";
    }
}
