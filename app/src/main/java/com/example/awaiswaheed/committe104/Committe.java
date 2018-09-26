package com.example.awaiswaheed.committe104;

public class Committe {
    public String name;
    public String owner_name;
    public Integer id;

    public Committe(String name, String owner_name, Integer id) {
        this.name = name;
        this.owner_name = owner_name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Committe() {}
}
