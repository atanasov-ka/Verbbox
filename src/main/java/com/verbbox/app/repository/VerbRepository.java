package com.verbbox.app.repository;

import com.verbbox.app.domain.Verb;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Verb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VerbRepository extends JpaRepository<Verb, Long> {

}
