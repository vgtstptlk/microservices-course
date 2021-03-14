package com.vgtstptlk.services.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Author {
    private String name;
    private String lastName;
    private String patronym;

    @JsonCreator
    public Author(@JsonProperty("name") String name, @JsonProperty("lastName") String lastName, @JsonProperty("patronym") String patronym) {
        this.name = name;
        this.lastName = lastName;
        this.patronym = patronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronym() {
        return patronym;
    }

    public void setPatronym(String patronym) {
        this.patronym = patronym;
    }
}
