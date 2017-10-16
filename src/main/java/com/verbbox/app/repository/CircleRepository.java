package com.verbbox.app.repository;

import com.verbbox.app.domain.Circle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Circle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CircleRepository extends JpaRepository<Circle, Long> {

}
