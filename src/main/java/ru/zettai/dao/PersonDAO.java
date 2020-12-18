package ru.zettai.dao;

import org.springframework.stereotype.Component;
import ru.zettai.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private List<Person> people;

    {
        people = new ArrayList<>();
        people.add(new Person(++PEOPLE_COUNT, "John"));
        people.add(new Person(++PEOPLE_COUNT, "Turk"));
        people.add(new Person(++PEOPLE_COUNT, "Lois"));
        people.add(new Person(++PEOPLE_COUNT, "Jane"));
    }

    //Чтение всего списка людей из БД
    public List<Person> index(){
        return people;
    }

    //Возращает конкретного человека из БД по указанному индексу
    public Person show(int id){
        return people.stream().filter(person -> person.getId()==id).findAny().orElse(null);
    }

    //Добавляет нового человека в БД
    public void save(Person person){
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }
}
