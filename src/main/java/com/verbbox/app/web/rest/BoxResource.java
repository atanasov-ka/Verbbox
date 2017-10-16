package com.verbbox.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.verbbox.app.domain.Box;

import com.verbbox.app.repository.BoxRepository;
import com.verbbox.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Box.
 */
@RestController
@RequestMapping("/api")
public class BoxResource {

    private final Logger log = LoggerFactory.getLogger(BoxResource.class);

    private static final String ENTITY_NAME = "box";

    private final BoxRepository boxRepository;

    public BoxResource(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    /**
     * POST  /boxes : Create a new box.
     *
     * @param box the box to create
     * @return the ResponseEntity with status 201 (Created) and with body the new box, or with status 400 (Bad Request) if the box has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boxes")
    @Timed
    public ResponseEntity<Box> createBox(@Valid @RequestBody Box box) throws URISyntaxException {
        log.debug("REST request to save Box : {}", box);
        if (box.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new box cannot already have an ID")).body(null);
        }
        Box result = boxRepository.save(box);
        return ResponseEntity.created(new URI("/api/boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boxes : Updates an existing box.
     *
     * @param box the box to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated box,
     * or with status 400 (Bad Request) if the box is not valid,
     * or with status 500 (Internal Server Error) if the box couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boxes")
    @Timed
    public ResponseEntity<Box> updateBox(@Valid @RequestBody Box box) throws URISyntaxException {
        log.debug("REST request to update Box : {}", box);
        if (box.getId() == null) {
            return createBox(box);
        }
        Box result = boxRepository.save(box);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, box.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boxes : get all the boxes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boxes in body
     */
    @GetMapping("/boxes")
    @Timed
    public List<Box> getAllBoxes() {
        log.debug("REST request to get all Boxes");
        return boxRepository.findAll();
        }

    /**
     * GET  /boxes/:id : get the "id" box.
     *
     * @param id the id of the box to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the box, or with status 404 (Not Found)
     */
    @GetMapping("/boxes/{id}")
    @Timed
    public ResponseEntity<Box> getBox(@PathVariable Long id) {
        log.debug("REST request to get Box : {}", id);
        Box box = boxRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(box));
    }

    /**
     * DELETE  /boxes/:id : delete the "id" box.
     *
     * @param id the id of the box to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteBox(@PathVariable Long id) {
        log.debug("REST request to delete Box : {}", id);
        boxRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
