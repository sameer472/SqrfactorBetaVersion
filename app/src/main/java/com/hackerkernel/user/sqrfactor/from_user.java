package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class from_user implements Serializable {
    private String email;
    private String name;
    private int id;
    private String username;

    public from_user(String email, String name, int id, String username) {
        this.email = email;
        this.name = name;
        this.id = id;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
