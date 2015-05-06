package com.mmz.mybatis.model;

/**
 * Created by dra on 15-5-5.
 */
public class User {

    private int id;

    private String last_name;

    public User() {}

    public User(String last_name) {
        this.last_name = last_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", last_name='" + last_name + '\'' +
                "} " + super.toString();
    }
}
