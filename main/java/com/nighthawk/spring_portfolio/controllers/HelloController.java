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
        List<String> j = new ArrayList<String>();
        List<Integer> boos = new ArrayList<Integer>();
        List<Integer> yays = new ArrayList<Integer>();
        for (Jokes x: jokes) {
            j.add(x.getJoke());
            boos.add(x.getBoohoo());
            yays.add(x.getHaha());
        }
        model.addAttribute("jokes", jokes);
        model.addAttribute("yays", yays);
        model.addAttribute("boos", boos);
        

        return "hello"; // This corresponds to the Thymeleaf template name (src/main/resources/templates/hello.html)
    }


    
}
