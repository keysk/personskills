package com.kevinthorne.personskills.repository;

import com.kevinthorne.personskills.model.PersonSkill;
import com.kevinthorne.personskills.model.PersonSkillKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonSkillRepository extends JpaRepository<PersonSkill, PersonSkillKey> {
    List<PersonSkill> findByIdPersonId(Long personId);

    List<PersonSkill> findByIdSkillId(Long skillId);
}