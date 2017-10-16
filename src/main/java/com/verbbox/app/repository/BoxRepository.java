package com.verbbox.app.repository;

import com.verbbox.app.domain.Box;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Box entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {

    @Query("select box from Box box where box.owner.login = ?#{principal.username}")
    List<Box> findByOwnerIsCurrentUser();

}
