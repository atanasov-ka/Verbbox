package com.verbbox.app.web.rest;

import com.verbbox.app.VerbboxApp;

import com.verbbox.app.domain.Play;
import com.verbbox.app.repository.PlayRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlayResource REST controller.
 *
 * @see PlayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VerbboxApp.class)
public class PlayResourceIntTest {

    private static final Integer DEFAULT_PROGRESS = 1;
    private static final Integer UPDATED_PROGRESS = 2;

    private static final LocalDate DEFAULT_LAST_ACTIVIRY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_ACTIVIRY = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PlayRepository playRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlayMockMvc;

    private Play play;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayResource playResource = new PlayResource(playRepository);
        this.restPlayMockMvc = MockMvcBuilders.standaloneSetup(playResource)
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
    public static Play createEntity(EntityManager em) {
        Play play = new Play()
            .progress(DEFAULT_PROGRESS)
            .lastActiviry(DEFAULT_LAST_ACTIVIRY);
        return play;
    }

    @Before
    public void initTest() {
        play = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlay() throws Exception {
        int databaseSizeBeforeCreate = playRepository.findAll().size();

        // Create the Play
        restPlayMockMvc.perform(post("/api/plays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(play)))
            .andExpect(status().isCreated());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeCreate + 1);
        Play testPlay = playList.get(playList.size() - 1);
        assertThat(testPlay.getProgress()).isEqualTo(DEFAULT_PROGRESS);
        assertThat(testPlay.getLastActiviry()).isEqualTo(DEFAULT_LAST_ACTIVIRY);
    }

    @Test
    @Transactional
    public void createPlayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playRepository.findAll().size();

        // Create the Play with an existing ID
        play.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayMockMvc.perform(post("/api/plays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(play)))
            .andExpect(status().isBadRequest());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlays() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);

        // Get all the playList
        restPlayMockMvc.perform(get("/api/plays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(play.getId().intValue())))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].lastActiviry").value(hasItem(DEFAULT_LAST_ACTIVIRY.toString())));
    }

    @Test
    @Transactional
    public void getPlay() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);

        // Get the play
        restPlayMockMvc.perform(get("/api/plays/{id}", play.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(play.getId().intValue()))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS))
            .andExpect(jsonPath("$.lastActiviry").value(DEFAULT_LAST_ACTIVIRY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlay() throws Exception {
        // Get the play
        restPlayMockMvc.perform(get("/api/plays/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlay() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);
        int databaseSizeBeforeUpdate = playRepository.findAll().size();

        // Update the play
        Play updatedPlay = playRepository.findOne(play.getId());
        updatedPlay
            .progress(UPDATED_PROGRESS)
            .lastActiviry(UPDATED_LAST_ACTIVIRY);

        restPlayMockMvc.perform(put("/api/plays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlay)))
            .andExpect(status().isOk());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate);
        Play testPlay = playList.get(playList.size() - 1);
        assertThat(testPlay.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testPlay.getLastActiviry()).isEqualTo(UPDATED_LAST_ACTIVIRY);
    }

    @Test
    @Transactional
    public void updateNonExistingPlay() throws Exception {
        int databaseSizeBeforeUpdate = playRepository.findAll().size();

        // Create the Play

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPlayMockMvc.perform(put("/api/plays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(play)))
            .andExpect(status().isCreated());

        // Validate the Play in the database
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePlay() throws Exception {
        // Initialize the database
        playRepository.saveAndFlush(play);
        int databaseSizeBeforeDelete = playRepository.findAll().size();

        // Get the play
        restPlayMockMvc.perform(delete("/api/plays/{id}", play.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Play> playList = playRepository.findAll();
        assertThat(playList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Play.class);
        Play play1 = new Play();
        play1.setId(1L);
        Play play2 = new Play();
        play2.setId(play1.getId());
        assertThat(play1).isEqualTo(play2);
        play2.setId(2L);
        assertThat(play1).isNotEqualTo(play2);
        play1.setId(null);
        assertThat(play1).isNotEqualTo(play2);
    }
}
