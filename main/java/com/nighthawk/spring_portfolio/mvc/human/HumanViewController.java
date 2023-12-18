package com.nighthawk.spring_portfolio.mvc.human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/mvc/person")
public class HumanViewController {
    // Autowired enables Control to connect HTML and POJO Object to database easily for CRUD
    @Autowired
    private HumanDetailsService repository;

    @GetMapping("/read")
    public String person(Model model) {
        List<Human> list = repository.listAll();
        model.addAttribute("list", list);
        return "person/read";
    }

    /*  The HTML template Forms and PersonForm attributes are bound
        @return - template for person form
        @param - Person Class
    */
    @GetMapping("/create")
    public String personAdd(Human person) {
        return "person/create";
    }

    /* Gathers the attributes filled out in the form, tests for and retrieves validation error
    @param - Person object with @Valid
    @param - BindingResult object
     */
    @PostMapping("/create")
    public String personSave(@Valid Human person, BindingResult bindingResult) {
        // Validation of Decorated PersonForm attributes
        if (bindingResult.hasErrors()) {
            return "person/create";
        }
        repository.save(person);
        // Redirect to next step
        return "redirect:/mvc/person/read";
    }

    @GetMapping("/update/{id}")
    public String personUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", repository.get(id));
        return "person/update";
    }

    @PostMapping("/update")
    public String personUpdateSave(@Valid Human person, BindingResult bindingResult) {
        // Validation of Decorated PersonForm attributes
        if (bindingResult.hasErrors()) {
            return "person/update";
        }
        repository.save(person);
        // Redirect to next step
        return "redirect:/mvc/person/read";
    }

    @GetMapping("/delete/{id}")
    public String humanDelete(@PathVariable long id) {
        repository.delete(id);
        return "redirect:/mvc/person/read";
    }

    @GetMapping("/search")
    public String person() {
        return "person/search";
    }

}
