package com.application.soundcloud.tables;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    // insert into roles (id, name) values (1, 'ROLE_USER');
    // insert into roles (id, name) values (2, 'ROLE_ADMIN');

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Role() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
