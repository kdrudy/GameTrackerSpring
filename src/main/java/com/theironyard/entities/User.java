package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by kdrudy on 12/22/16.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = false)
    public String password;

    public User() {

    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
