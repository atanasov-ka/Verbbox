package com.verbbox.app.web.rest;

import com.verbbox.app.VerbboxApp;

import com.verbbox.app.domain.Box;
import com.verbbox.app.repository.BoxRepository;
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
 * Test class for the BoxResource REST controller.
 *
 * @see BoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VerbboxApp.class)
public class BoxResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoxMockMvc;

    private Box box;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoxResource boxResource = new BoxResource(boxRepository);
        this.restBoxMockMvc = MockMvcBuilders.standaloneSetup(boxResource)
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
    public static Box createEntity(EntityManager em) {
        Box box = new Box()
            .name(DEFAULT_NAME)
            .firstLanguage(DEFAULT_FIRST_LANGUAGE)
            .secondLanguage(DEFAULT_SECOND_LANGUAGE)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED);
        return box;
    }

    @Before
    public void initTest() {
        box = createEntity(em);
    }

    @Test
    @Transactional
    public void createBox() throws Exception {
        int databaseSizeBeforeCreate = boxRepository.findAll().size();

        // Create the Box
        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isCreated());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeCreate + 1);
        Box testBox = boxList.get(boxList.size() - 1);
        assertThat(testBox.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBox.getFirstLanguage()).isEqualTo(DEFAULT_FIRST_LANGUAGE);
        assertThat(testBox.getSecondLanguage()).isEqualTo(DEFAULT_SECOND_LANGUAGE);
        assertThat(testBox.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBox.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    public void createBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boxRepository.findAll().size();

        // Create the Box with an existing ID
        box.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isBadRequest());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = boxRepository.findAll().size();
        // set the field null
        box.setName(null);

        // Create the Box, which fails.

        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isBadRequest());

        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = boxRepository.findAll().size();
        // set the field null
        box.setFirstLanguage(null);

        // Create the Box, which fails.

        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isBadRequest());

        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = boxRepository.findAll().size();
        // set the field null
        box.setSecondLanguage(null);

        // Create the Box, which fails.

        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isBadRequest());

        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoxes() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);

        // Get all the boxList
        restBoxMockMvc.perform(get("/api/boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(box.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstLanguage").value(hasItem(DEFAULT_FIRST_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].secondLanguage").value(hasItem(DEFAULT_SECOND_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    public void getBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);

        // Get the box
        restBoxMockMvc.perform(get("/api/boxes/{id}", box.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(box.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.firstLanguage").value(DEFAULT_FIRST_LANGUAGE.toString()))
            .andExpect(jsonPath("$.secondLanguage").value(DEFAULT_SECOND_LANGUAGE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    public void getNonExistingBox() throws Exception {
        // Get the box
        restBoxMockMvc.perform(get("/api/boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);
        int databaseSizeBeforeUpdate = boxRepository.findAll().size();

        // Update the box
        Box updatedBox = boxRepository.findOne(box.getId());
        updatedBox
            .name(UPDATED_NAME)
            .firstLanguage(UPDATED_FIRST_LANGUAGE)
            .secondLanguage(UPDATED_SECOND_LANGUAGE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED);

        restBoxMockMvc.perform(put("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBox)))
            .andExpect(status().isOk());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeUpdate);
        Box testBox = boxList.get(boxList.size() - 1);
        assertThat(testBox.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBox.getFirstLanguage()).isEqualTo(UPDATED_FIRST_LANGUAGE);
        assertThat(testBox.getSecondLanguage()).isEqualTo(UPDATED_SECOND_LANGUAGE);
        assertThat(testBox.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBox.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingBox() throws Exception {
        int databaseSizeBeforeUpdate = boxRepository.findAll().size();

        // Create the Box

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoxMockMvc.perform(put("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isCreated());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBox() throws Exception {
        // Initialize the database
        boxRepository.saveAndFlush(box);
        int databaseSizeBeforeDelete = boxRepository.findAll().size();

        // Get the box
        restBoxMockMvc.perform(delete("/api/boxes/{id}", box.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Box.class);
        Box box1 = new Box();
        box1.setId(1L);
        Box box2 = new Box();
        box2.setId(box1.getId());
        assertThat(box1).isEqualTo(box2);
        box2.setId(2L);
        assertThat(box1).isNotEqualTo(box2);
        box1.setId(null);
        assertThat(box1).isNotEqualTo(box2);
    }
}
