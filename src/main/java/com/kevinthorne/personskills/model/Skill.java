package com.kevinthorne.personskills.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Skill {
    @Id
    @GeneratedValue
    private Long skillId;

    @NotNull
    private String name;

    public Skill() {
        super();
    }

    public Skill(Long id, String name) {
        super();
        this.skillId = id;
        this.name = name;

    }

    public Long getId() {
        return skillId;
    }
    public void setId(Long id) {
        this.skillId = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
