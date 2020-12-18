package ru.zettai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zettai.dao.PersonDAO;
import ru.zettai.models.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    //получаем всех людей из DAO и передадим на отображение в представление
    public String index(Model model){
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    //получаем одного человека по id из DAO и отобразим его
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    //получаем из браузера по указанному адресу html форму для создания нового человека
    public String newPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }

    @PostMapping()
    //создаём нового человека по полученной html форме и передаём его в базу данных
    public String create(@ModelAttribute("person") Person person, Model model){
        personDAO.save(person);
        return "redirect:/people"; //перенаправление на базовую страницу со всеми людьми с помошью redirect
    }





}
