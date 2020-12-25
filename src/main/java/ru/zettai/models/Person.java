package ru.zettai.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

public class Person {
    private long id;

    @NotEmpty(message = "Name shouldn't be empty!")
    @Size(max = 30, message = "You overtake range of characters (2-30)")
    private String name;

    @Min(value = 0, message = "Age can not be negative")
    private int age;

    @NotEmpty(message = "Email shouldn't be empty")
    @Email(message = "Email should be correct!")
    private String email;

    // нужен для создания пустого шаблона человека
    public Person(){}

    public Person(long id, String name, int age, String email){
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
  }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
