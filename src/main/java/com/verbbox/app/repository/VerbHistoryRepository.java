package com.verbbox.app.repository;

import com.verbbox.app.domain.VerbHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VerbHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VerbHistoryRepository extends JpaRepository<VerbHistory, Long> {

}
