package com.verbbox.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.verbbox.app.domain.Play;

import com.verbbox.app.repository.PlayRepository;
import com.verbbox.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Play.
 */
@RestController
@RequestMapping("/api")
public class PlayResource {

    private final Logger log = LoggerFactory.getLogger(PlayResource.class);

    private static final String ENTITY_NAME = "play";

    private final PlayRepository playRepository;

    public PlayResource(PlayRepository playRepository) {
        this.playRepository = playRepository;
    }

    /**
     * POST  /plays : Create a new play.
     *
     * @param play the play to create
     * @return the ResponseEntity with status 201 (Created) and with body the new play, or with status 400 (Bad Request) if the play has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/plays")
    @Timed
    public ResponseEntity<Play> createPlay(@RequestBody Play play) throws URISyntaxException {
        log.debug("REST request to save Play : {}", play);
        if (play.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new play cannot already have an ID")).body(null);
        }
        Play result = playRepository.save(play);
        return ResponseEntity.created(new URI("/api/plays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /plays : Updates an existing play.
     *
     * @param play the play to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated play,
     * or with status 400 (Bad Request) if the play is not valid,
     * or with status 500 (Internal Server Error) if the play couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/plays")
    @Timed
    public ResponseEntity<Play> updatePlay(@RequestBody Play play) throws URISyntaxException {
        log.debug("REST request to update Play : {}", play);
        if (play.getId() == null) {
            return createPlay(play);
        }
        Play result = playRepository.save(play);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, play.getId().toString()))
            .body(result);
    }

    /**
     * GET  /plays : get all the plays.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of plays in body
     */
    @GetMapping("/plays")
    @Timed
    public List<Play> getAllPlays() {
        log.debug("REST request to get all Plays");
        return playRepository.findAll();
        }

    /**
     * GET  /plays/:id : get the "id" play.
     *
     * @param id the id of the play to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the play, or with status 404 (Not Found)
     */
    @GetMapping("/plays/{id}")
    @Timed
    public ResponseEntity<Play> getPlay(@PathVariable Long id) {
        log.debug("REST request to get Play : {}", id);
        Play play = playRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(play));
    }

    /**
     * DELETE  /plays/:id : delete the "id" play.
     *
     * @param id the id of the play to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/plays/{id}")
    @Timed
    public ResponseEntity<Void> deletePlay(@PathVariable Long id) {
        log.debug("REST request to delete Play : {}", id);
        playRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
