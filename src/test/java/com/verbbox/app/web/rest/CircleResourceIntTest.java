package com.verbbox.app.web.rest;

import com.verbbox.app.VerbboxApp;

import com.verbbox.app.domain.Circle;
import com.verbbox.app.repository.CircleRepository;
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
 * Test class for the CircleResource REST controller.
 *
 * @see CircleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VerbboxApp.class)
public class CircleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CircleRepository circleRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCircleMockMvc;

    private Circle circle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CircleResource circleResource = new CircleResource(circleRepository);
        this.restCircleMockMvc = MockMvcBuilders.standaloneSetup(circleResource)
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
    public static Circle createEntity(EntityManager em) {
        Circle circle = new Circle()
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED);
        return circle;
    }

    @Before
    public void initTest() {
        circle = createEntity(em);
    }

    @Test
    @Transactional
    public void createCircle() throws Exception {
        int databaseSizeBeforeCreate = circleRepository.findAll().size();

        // Create the Circle
        restCircleMockMvc.perform(post("/api/circles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circle)))
            .andExpect(status().isCreated());

        // Validate the Circle in the database
        List<Circle> circleList = circleRepository.findAll();
        assertThat(circleList).hasSize(databaseSizeBeforeCreate + 1);
        Circle testCircle = circleList.get(circleList.size() - 1);
        assertThat(testCircle.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCircle.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    public void createCircleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = circleRepository.findAll().size();

        // Create the Circle with an existing ID
        circle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCircleMockMvc.perform(post("/api/circles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circle)))
            .andExpect(status().isBadRequest());

        // Validate the Circle in the database
        List<Circle> circleList = circleRepository.findAll();
        assertThat(circleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCircles() throws Exception {
        // Initialize the database
        circleRepository.saveAndFlush(circle);

        // Get all the circleList
        restCircleMockMvc.perform(get("/api/circles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    public void getCircle() throws Exception {
        // Initialize the database
        circleRepository.saveAndFlush(circle);

        // Get the circle
        restCircleMockMvc.perform(get("/api/circles/{id}", circle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(circle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    public void getNonExistingCircle() throws Exception {
        // Get the circle
        restCircleMockMvc.perform(get("/api/circles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCircle() throws Exception {
        // Initialize the database
        circleRepository.saveAndFlush(circle);
        int databaseSizeBeforeUpdate = circleRepository.findAll().size();

        // Update the circle
        Circle updatedCircle = circleRepository.findOne(circle.getId());
        updatedCircle
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED);

        restCircleMockMvc.perform(put("/api/circles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCircle)))
            .andExpect(status().isOk());

        // Validate the Circle in the database
        List<Circle> circleList = circleRepository.findAll();
        assertThat(circleList).hasSize(databaseSizeBeforeUpdate);
        Circle testCircle = circleList.get(circleList.size() - 1);
        assertThat(testCircle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCircle.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingCircle() throws Exception {
        int databaseSizeBeforeUpdate = circleRepository.findAll().size();

        // Create the Circle

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCircleMockMvc.perform(put("/api/circles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circle)))
            .andExpect(status().isCreated());

        // Validate the Circle in the database
        List<Circle> circleList = circleRepository.findAll();
        assertThat(circleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCircle() throws Exception {
        // Initialize the database
        circleRepository.saveAndFlush(circle);
        int databaseSizeBeforeDelete = circleRepository.findAll().size();

        // Get the circle
        restCircleMockMvc.perform(delete("/api/circles/{id}", circle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Circle> circleList = circleRepository.findAll();
        assertThat(circleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Circle.class);
        Circle circle1 = new Circle();
        circle1.setId(1L);
        Circle circle2 = new Circle();
        circle2.setId(circle1.getId());
        assertThat(circle1).isEqualTo(circle2);
        circle2.setId(2L);
        assertThat(circle1).isNotEqualTo(circle2);
        circle1.setId(null);
        assertThat(circle1).isNotEqualTo(circle2);
    }
}
