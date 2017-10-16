package com.verbbox.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.verbbox.app.domain.Verb;

import com.verbbox.app.repository.VerbRepository;
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
 * REST controller for managing Verb.
 */
@RestController
@RequestMapping("/api")
public class VerbResource {

    private final Logger log = LoggerFactory.getLogger(VerbResource.class);

    private static final String ENTITY_NAME = "verb";

    private final VerbRepository verbRepository;

    public VerbResource(VerbRepository verbRepository) {
        this.verbRepository = verbRepository;
    }

    /**
     * POST  /verbs : Create a new verb.
     *
     * @param verb the verb to create
     * @return the ResponseEntity with status 201 (Created) and with body the new verb, or with status 400 (Bad Request) if the verb has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/verbs")
    @Timed
    public ResponseEntity<Verb> createVerb(@Valid @RequestBody Verb verb) throws URISyntaxException {
        log.debug("REST request to save Verb : {}", verb);
        if (verb.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new verb cannot already have an ID")).body(null);
        }
        Verb result = verbRepository.save(verb);
        return ResponseEntity.created(new URI("/api/verbs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /verbs : Updates an existing verb.
     *
     * @param verb the verb to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated verb,
     * or with status 400 (Bad Request) if the verb is not valid,
     * or with status 500 (Internal Server Error) if the verb couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/verbs")
    @Timed
    public ResponseEntity<Verb> updateVerb(@Valid @RequestBody Verb verb) throws URISyntaxException {
        log.debug("REST request to update Verb : {}", verb);
        if (verb.getId() == null) {
            return createVerb(verb);
        }
        Verb result = verbRepository.save(verb);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, verb.getId().toString()))
            .body(result);
    }

    /**
     * GET  /verbs : get all the verbs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of verbs in body
     */
    @GetMapping("/verbs")
    @Timed
    public List<Verb> getAllVerbs() {
        log.debug("REST request to get all Verbs");
        return verbRepository.findAll();
        }

    /**
     * GET  /verbs/:id : get the "id" verb.
     *
     * @param id the id of the verb to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the verb, or with status 404 (Not Found)
     */
    @GetMapping("/verbs/{id}")
    @Timed
    public ResponseEntity<Verb> getVerb(@PathVariable Long id) {
        log.debug("REST request to get Verb : {}", id);
        Verb verb = verbRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(verb));
    }

    /**
     * DELETE  /verbs/:id : delete the "id" verb.
     *
     * @param id the id of the verb to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/verbs/{id}")
    @Timed
    public ResponseEntity<Void> deleteVerb(@PathVariable Long id) {
        log.debug("REST request to delete Verb : {}", id);
        verbRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
