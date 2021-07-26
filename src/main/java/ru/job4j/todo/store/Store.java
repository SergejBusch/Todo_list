package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.UserItem;

import java.util.Collection;

public interface Store {

    Collection<Item> getAllTasks(int id);

    Collection<Item> getUnfinishedTasks(int id);

    void saveOrUpdate(Item item);

    UserItem getUserByEmail(String name);

    void saveOrUpdate(UserItem userItem);

}
