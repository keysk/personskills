package com.kevinthorne.personskills.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class PersonSkill {

    @EmbeddedId
    PersonSkillKey id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SkillLevelsEnum skillLevel;

    public PersonSkill() {
    }

    public PersonSkill(PersonSkillKey id, SkillLevelsEnum skillLevel) {
        this.id = id;
        this.skillLevel = skillLevel;
    }

    public PersonSkillKey getId() {
        return id;
    }
    public void setId(PersonSkillKey id) {
        this.id = id;
    }

    //public String getSkillLevel() { return skillLevel.getValue(); }
    public SkillLevelsEnum getSkillLevel() { return skillLevel; }
    public void setSkillLevel(SkillLevelsEnum skillLevel) { this.skillLevel = skillLevel; }

    public enum SkillLevelsEnum {
        EXPERT("Expert"),
        PRACTITIONER("Practitioner"),
        WORKING("Working"),
        AWARENESS("Awareness");

        private String value;

        SkillLevelsEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static SkillLevelsEnum fromValue(String value) {
            for (SkillLevelsEnum b : SkillLevelsEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
