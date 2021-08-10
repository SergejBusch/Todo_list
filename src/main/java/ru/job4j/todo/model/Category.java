package ru.job4j.todo.model;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public static Category of(String name) {
        var category = new Category();
        category.name = name;
        return category;
    }
}
