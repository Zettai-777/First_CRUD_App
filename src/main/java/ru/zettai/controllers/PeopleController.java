package ru.zettai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zettai.dao.PersonDAO;
import ru.zettai.models.Person;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

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
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        //проверка на ошибки валидации
        if(bindingResult.hasErrors())
            return "people/new";
        personDAO.save(person);
        return "redirect:/people"; //перенаправление на базовую страницу со всеми людьми с помошью redirect
    }

    @GetMapping("/{id}/edit")
    //получаем html страницу человека для редактирования по его id номеру
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    // вытаскиваем из модели форму человека и из адреса его id, затем обновляем в DAO информацию по человеку
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors())
            return "people/edit";
        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    //удаление человека по его id
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return "redirect:/people";
    }

}
