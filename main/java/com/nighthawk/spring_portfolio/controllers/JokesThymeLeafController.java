package com.nighthawk.spring_portfolio.controllers;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nighthawk.spring_portfolio.mvc.jokes.JokesJpaRepository;
import com.nighthawk.spring_portfolio.mvc.jokes.Jokes;

@Controller
public class HelloController {

    @Autowired
    private JokesJpaRepository repository;

    @GetMapping("/hello")
    public String hello(Model model) {
        List<Jokes> jokes = repository.findAll();
        model.addAttribute("jokes", jokes);
        

        return "thyme"; // This corresponds to the Thymeleaf template name (src/main/resources/templates/hello.html)
    }


    
}
