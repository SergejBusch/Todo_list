package ru.job4j.todo.model;

import javax.persistence.*;

@Entity
@Table(name = "user_item")
public class UserItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;

    public UserItem(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public UserItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserItem userItem = (UserItem) o;

        if (id != userItem.id) {
            return false;
        }
        if (!email.equals(userItem.email)) {
            return false;
        }
        return password.equals(userItem.password);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
