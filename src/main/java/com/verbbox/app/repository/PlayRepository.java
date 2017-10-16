package com.verbbox.app.repository;

import com.verbbox.app.domain.Play;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Play entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayRepository extends JpaRepository<Play, Long> {

    @Query("select play from Play play where play.player.login = ?#{principal.username}")
    List<Play> findByPlayerIsCurrentUser();

}
