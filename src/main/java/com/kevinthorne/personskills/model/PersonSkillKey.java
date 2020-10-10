package com.kevinthorne.personskills.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersonSkillKey implements Serializable {

    @Column(name = "person_id")
    @NotNull
    Long personId;

    @Column(name = "skill_id")
    @NotNull
    Long skillId;

    public PersonSkillKey() {
    }

    public PersonSkillKey(Long personId, Long skillId) {
        this.personId = personId;
        this.skillId = skillId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonSkillKey that = (PersonSkillKey) o;
        return personId.equals(that.personId) &&
                skillId.equals(that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, skillId);
    }
}
