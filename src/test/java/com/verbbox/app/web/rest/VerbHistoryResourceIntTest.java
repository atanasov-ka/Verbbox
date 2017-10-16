package com.verbbox.app.web.rest;

import com.verbbox.app.VerbboxApp;

import com.verbbox.app.domain.VerbHistory;
import com.verbbox.app.repository.VerbHistoryRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VerbHistoryResource REST controller.
 *
 * @see VerbHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VerbboxApp.class)
public class VerbHistoryResourceIntTest {

    private static final Integer DEFAULT_FRONT_BACK_YES = 1;
    private static final Integer UPDATED_FRONT_BACK_YES = 2;

    private static final Integer DEFAULT_BACK_FRONT_YES = 1;
    private static final Integer UPDATED_BACK_FRONT_YES = 2;

    private static final Integer DEFAULT_FRONT_BACK_NO = 1;
    private static final Integer UPDATED_FRONT_BACK_NO = 2;

    private static final Integer DEFAULT_BACK_FRONT_NO = 1;
    private static final Integer UPDATED_BACK_FRONT_NO = 2;

    @Autowired
    private VerbHistoryRepository verbHistoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVerbHistoryMockMvc;

    private VerbHistory verbHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VerbHistoryResource verbHistoryResource = new VerbHistoryResource(verbHistoryRepository);
        this.restVerbHistoryMockMvc = MockMvcBuilders.standaloneSetup(verbHistoryResource)
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
    public static VerbHistory createEntity(EntityManager em) {
        VerbHistory verbHistory = new VerbHistory()
            .frontBackYes(DEFAULT_FRONT_BACK_YES)
            .backFrontYes(DEFAULT_BACK_FRONT_YES)
            .frontBackNo(DEFAULT_FRONT_BACK_NO)
            .backFrontNo(DEFAULT_BACK_FRONT_NO);
        return verbHistory;
    }

    @Before
    public void initTest() {
        verbHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createVerbHistory() throws Exception {
        int databaseSizeBeforeCreate = verbHistoryRepository.findAll().size();

        // Create the VerbHistory
        restVerbHistoryMockMvc.perform(post("/api/verb-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verbHistory)))
            .andExpect(status().isCreated());

        // Validate the VerbHistory in the database
        List<VerbHistory> verbHistoryList = verbHistoryRepository.findAll();
        assertThat(verbHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        VerbHistory testVerbHistory = verbHistoryList.get(verbHistoryList.size() - 1);
        assertThat(testVerbHistory.getFrontBackYes()).isEqualTo(DEFAULT_FRONT_BACK_YES);
        assertThat(testVerbHistory.getBackFrontYes()).isEqualTo(DEFAULT_BACK_FRONT_YES);
        assertThat(testVerbHistory.getFrontBackNo()).isEqualTo(DEFAULT_FRONT_BACK_NO);
        assertThat(testVerbHistory.getBackFrontNo()).isEqualTo(DEFAULT_BACK_FRONT_NO);
    }

    @Test
    @Transactional
    public void createVerbHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = verbHistoryRepository.findAll().size();

        // Create the VerbHistory with an existing ID
        verbHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVerbHistoryMockMvc.perform(post("/api/verb-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verbHistory)))
            .andExpect(status().isBadRequest());

        // Validate the VerbHistory in the database
        List<VerbHistory> verbHistoryList = verbHistoryRepository.findAll();
        assertThat(verbHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVerbHistories() throws Exception {
        // Initialize the database
        verbHistoryRepository.saveAndFlush(verbHistory);

        // Get all the verbHistoryList
        restVerbHistoryMockMvc.perform(get("/api/verb-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(verbHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].frontBackYes").value(hasItem(DEFAULT_FRONT_BACK_YES)))
            .andExpect(jsonPath("$.[*].backFrontYes").value(hasItem(DEFAULT_BACK_FRONT_YES)))
            .andExpect(jsonPath("$.[*].frontBackNo").value(hasItem(DEFAULT_FRONT_BACK_NO)))
            .andExpect(jsonPath("$.[*].backFrontNo").value(hasItem(DEFAULT_BACK_FRONT_NO)));
    }

    @Test
    @Transactional
    public void getVerbHistory() throws Exception {
        // Initialize the database
        verbHistoryRepository.saveAndFlush(verbHistory);

        // Get the verbHistory
        restVerbHistoryMockMvc.perform(get("/api/verb-histories/{id}", verbHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(verbHistory.getId().intValue()))
            .andExpect(jsonPath("$.frontBackYes").value(DEFAULT_FRONT_BACK_YES))
            .andExpect(jsonPath("$.backFrontYes").value(DEFAULT_BACK_FRONT_YES))
            .andExpect(jsonPath("$.frontBackNo").value(DEFAULT_FRONT_BACK_NO))
            .andExpect(jsonPath("$.backFrontNo").value(DEFAULT_BACK_FRONT_NO));
    }

    @Test
    @Transactional
    public void getNonExistingVerbHistory() throws Exception {
        // Get the verbHistory
        restVerbHistoryMockMvc.perform(get("/api/verb-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVerbHistory() throws Exception {
        // Initialize the database
        verbHistoryRepository.saveAndFlush(verbHistory);
        int databaseSizeBeforeUpdate = verbHistoryRepository.findAll().size();

        // Update the verbHistory
        VerbHistory updatedVerbHistory = verbHistoryRepository.findOne(verbHistory.getId());
        updatedVerbHistory
            .frontBackYes(UPDATED_FRONT_BACK_YES)
            .backFrontYes(UPDATED_BACK_FRONT_YES)
            .frontBackNo(UPDATED_FRONT_BACK_NO)
            .backFrontNo(UPDATED_BACK_FRONT_NO);

        restVerbHistoryMockMvc.perform(put("/api/verb-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVerbHistory)))
            .andExpect(status().isOk());

        // Validate the VerbHistory in the database
        List<VerbHistory> verbHistoryList = verbHistoryRepository.findAll();
        assertThat(verbHistoryList).hasSize(databaseSizeBeforeUpdate);
        VerbHistory testVerbHistory = verbHistoryList.get(verbHistoryList.size() - 1);
        assertThat(testVerbHistory.getFrontBackYes()).isEqualTo(UPDATED_FRONT_BACK_YES);
        assertThat(testVerbHistory.getBackFrontYes()).isEqualTo(UPDATED_BACK_FRONT_YES);
        assertThat(testVerbHistory.getFrontBackNo()).isEqualTo(UPDATED_FRONT_BACK_NO);
        assertThat(testVerbHistory.getBackFrontNo()).isEqualTo(UPDATED_BACK_FRONT_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingVerbHistory() throws Exception {
        int databaseSizeBeforeUpdate = verbHistoryRepository.findAll().size();

        // Create the VerbHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVerbHistoryMockMvc.perform(put("/api/verb-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verbHistory)))
            .andExpect(status().isCreated());

        // Validate the VerbHistory in the database
        List<VerbHistory> verbHistoryList = verbHistoryRepository.findAll();
        assertThat(verbHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVerbHistory() throws Exception {
        // Initialize the database
        verbHistoryRepository.saveAndFlush(verbHistory);
        int databaseSizeBeforeDelete = verbHistoryRepository.findAll().size();

        // Get the verbHistory
        restVerbHistoryMockMvc.perform(delete("/api/verb-histories/{id}", verbHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VerbHistory> verbHistoryList = verbHistoryRepository.findAll();
        assertThat(verbHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VerbHistory.class);
        VerbHistory verbHistory1 = new VerbHistory();
        verbHistory1.setId(1L);
        VerbHistory verbHistory2 = new VerbHistory();
        verbHistory2.setId(verbHistory1.getId());
        assertThat(verbHistory1).isEqualTo(verbHistory2);
        verbHistory2.setId(2L);
        assertThat(verbHistory1).isNotEqualTo(verbHistory2);
        verbHistory1.setId(null);
        assertThat(verbHistory1).isNotEqualTo(verbHistory2);
    }
}
