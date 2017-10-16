package com.verbbox.app.web.rest;

import com.verbbox.app.VerbboxApp;

import com.verbbox.app.domain.Verb;
import com.verbbox.app.repository.VerbRepository;
import com.verbbox.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.verbbox.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VerbResource REST controller.
 *
 * @see VerbResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VerbboxApp.class)
public class VerbResourceIntTest {

    private static final String DEFAULT_FRONT = "AAAAAAAAAA";
    private static final String UPDATED_FRONT = "BBBBBBBBBB";

    private static final String DEFAULT_BACK = "AAAAAAAAAA";
    private static final String UPDATED_BACK = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private VerbRepository verbRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVerbMockMvc;

    private Verb verb;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VerbResource verbResource = new VerbResource(verbRepository);
        this.restVerbMockMvc = MockMvcBuilders.standaloneSetup(verbResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Verb createEntity(EntityManager em) {
        Verb verb = new Verb()
            .front(DEFAULT_FRONT)
            .back(DEFAULT_BACK)
            .created(DEFAULT_CREATED);
        return verb;
    }

    @Before
    public void initTest() {
        verb = createEntity(em);
    }

    @Test
    @Transactional
    public void createVerb() throws Exception {
        int databaseSizeBeforeCreate = verbRepository.findAll().size();

        // Create the Verb
        restVerbMockMvc.perform(post("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isCreated());

        // Validate the Verb in the database
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeCreate + 1);
        Verb testVerb = verbList.get(verbList.size() - 1);
        assertThat(testVerb.getFront()).isEqualTo(DEFAULT_FRONT);
        assertThat(testVerb.getBack()).isEqualTo(DEFAULT_BACK);
        assertThat(testVerb.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    public void createVerbWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = verbRepository.findAll().size();

        // Create the Verb with an existing ID
        verb.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVerbMockMvc.perform(post("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isBadRequest());

        // Validate the Verb in the database
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFrontIsRequired() throws Exception {
        int databaseSizeBeforeTest = verbRepository.findAll().size();
        // set the field null
        verb.setFront(null);

        // Create the Verb, which fails.

        restVerbMockMvc.perform(post("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isBadRequest());

        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBackIsRequired() throws Exception {
        int databaseSizeBeforeTest = verbRepository.findAll().size();
        // set the field null
        verb.setBack(null);

        // Create the Verb, which fails.

        restVerbMockMvc.perform(post("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isBadRequest());

        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVerbs() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        // Get all the verbList
        restVerbMockMvc.perform(get("/api/verbs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(verb.getId().intValue())))
            .andExpect(jsonPath("$.[*].front").value(hasItem(DEFAULT_FRONT.toString())))
            .andExpect(jsonPath("$.[*].back").value(hasItem(DEFAULT_BACK.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    public void getVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        // Get the verb
        restVerbMockMvc.perform(get("/api/verbs/{id}", verb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(verb.getId().intValue()))
            .andExpect(jsonPath("$.front").value(DEFAULT_FRONT.toString()))
            .andExpect(jsonPath("$.back").value(DEFAULT_BACK.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    public void getNonExistingVerb() throws Exception {
        // Get the verb
        restVerbMockMvc.perform(get("/api/verbs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);
        int databaseSizeBeforeUpdate = verbRepository.findAll().size();

        // Update the verb
        Verb updatedVerb = verbRepository.findOne(verb.getId());
        updatedVerb
            .front(UPDATED_FRONT)
            .back(UPDATED_BACK)
            .created(UPDATED_CREATED);

        restVerbMockMvc.perform(put("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVerb)))
            .andExpect(status().isOk());

        // Validate the Verb in the database
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeUpdate);
        Verb testVerb = verbList.get(verbList.size() - 1);
        assertThat(testVerb.getFront()).isEqualTo(UPDATED_FRONT);
        assertThat(testVerb.getBack()).isEqualTo(UPDATED_BACK);
        assertThat(testVerb.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingVerb() throws Exception {
        int databaseSizeBeforeUpdate = verbRepository.findAll().size();

        // Create the Verb

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVerbMockMvc.perform(put("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isCreated());

        // Validate the Verb in the database
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);
        int databaseSizeBeforeDelete = verbRepository.findAll().size();

        // Get the verb
        restVerbMockMvc.perform(delete("/api/verbs/{id}", verb.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Verb.class);
        Verb verb1 = new Verb();
        verb1.setId(1L);
        Verb verb2 = new Verb();
        verb2.setId(verb1.getId());
        assertThat(verb1).isEqualTo(verb2);
        verb2.setId(2L);
        assertThat(verb1).isNotEqualTo(verb2);
        verb1.setId(null);
        assertThat(verb1).isNotEqualTo(verb2);
    }
}
