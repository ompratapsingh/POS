package com.mak.pos.Model.POJO;

import java.io.Serializable;

public class TableInfo implements Serializable {

    private String code;

    private String name;

    private String availablity;

    private String location;

    private String users;
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String code) {
        this.amount = amount;
    }
 public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailablity() {
        return availablity;
    }

    public void setAvailablity(String availablity) {
        this.availablity = availablity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ClassPojo [code = " + code + ", name = " + name + ", availablity = " + availablity + ", location = " + location + ", users = " + users + "]";
    }
}