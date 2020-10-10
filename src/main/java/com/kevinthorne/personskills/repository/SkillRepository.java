package com.kevinthorne.personskills.repository;

import com.kevinthorne.personskills.model.Skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
