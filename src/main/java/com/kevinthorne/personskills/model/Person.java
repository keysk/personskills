package com.kevinthorne.personskills.model;

import javax.validation.constraints.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long personId;

    @NotNull
    private String firstName;

    @NotNull
    private String surname;


    public Person() {
        super();
    }

    public Person(Long id, String firstName, String surname) {
        super();
        this.personId = id;
        this.firstName = firstName;
        this.surname = surname;
    }

    public Long getId() {
        return personId;
    }
    public void setId(Long id) {
        this.personId = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String name) {
        this.firstName = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurnameName(String name) {
        this.surname = name;
    }
}
