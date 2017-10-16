package com.verbbox.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.verbbox.app.domain.VerbHistory;

import com.verbbox.app.repository.VerbHistoryRepository;
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
 * REST controller for managing VerbHistory.
 */
@RestController
@RequestMapping("/api")
public class VerbHistoryResource {

    private final Logger log = LoggerFactory.getLogger(VerbHistoryResource.class);

    private static final String ENTITY_NAME = "verbHistory";

    private final VerbHistoryRepository verbHistoryRepository;

    public VerbHistoryResource(VerbHistoryRepository verbHistoryRepository) {
        this.verbHistoryRepository = verbHistoryRepository;
    }

    /**
     * POST  /verb-histories : Create a new verbHistory.
     *
     * @param verbHistory the verbHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new verbHistory, or with status 400 (Bad Request) if the verbHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/verb-histories")
    @Timed
    public ResponseEntity<VerbHistory> createVerbHistory(@RequestBody VerbHistory verbHistory) throws URISyntaxException {
        log.debug("REST request to save VerbHistory : {}", verbHistory);
        if (verbHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new verbHistory cannot already have an ID")).body(null);
        }
        VerbHistory result = verbHistoryRepository.save(verbHistory);
        return ResponseEntity.created(new URI("/api/verb-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /verb-histories : Updates an existing verbHistory.
     *
     * @param verbHistory the verbHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated verbHistory,
     * or with status 400 (Bad Request) if the verbHistory is not valid,
     * or with status 500 (Internal Server Error) if the verbHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/verb-histories")
    @Timed
    public ResponseEntity<VerbHistory> updateVerbHistory(@RequestBody VerbHistory verbHistory) throws URISyntaxException {
        log.debug("REST request to update VerbHistory : {}", verbHistory);
        if (verbHistory.getId() == null) {
            return createVerbHistory(verbHistory);
        }
        VerbHistory result = verbHistoryRepository.save(verbHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, verbHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /verb-histories : get all the verbHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of verbHistories in body
     */
    @GetMapping("/verb-histories")
    @Timed
    public List<VerbHistory> getAllVerbHistories() {
        log.debug("REST request to get all VerbHistories");
        return verbHistoryRepository.findAll();
        }

    /**
     * GET  /verb-histories/:id : get the "id" verbHistory.
     *
     * @param id the id of the verbHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the verbHistory, or with status 404 (Not Found)
     */
    @GetMapping("/verb-histories/{id}")
    @Timed
    public ResponseEntity<VerbHistory> getVerbHistory(@PathVariable Long id) {
        log.debug("REST request to get VerbHistory : {}", id);
        VerbHistory verbHistory = verbHistoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(verbHistory));
    }

    /**
     * DELETE  /verb-histories/:id : delete the "id" verbHistory.
     *
     * @param id the id of the verbHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/verb-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteVerbHistory(@PathVariable Long id) {
        log.debug("REST request to delete VerbHistory : {}", id);
        verbHistoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
