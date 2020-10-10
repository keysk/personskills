package com.kevinthorne.personskills.repository;

import com.kevinthorne.personskills.model.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository  extends JpaRepository<Person, Long> {
}
