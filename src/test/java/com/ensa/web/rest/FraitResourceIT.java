package com.ensa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ensa.IntegrationTest;
import com.ensa.domain.Frait;
import com.ensa.repository.FraitRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FraitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FraitResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final String ENTITY_API_URL = "/api/fraits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FraitRepository fraitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFraitMockMvc;

    private Frait frait;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frait createEntity(EntityManager em) {
        Frait frait = new Frait().type(DEFAULT_TYPE).montant(DEFAULT_MONTANT);
        return frait;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frait createUpdatedEntity(EntityManager em) {
        Frait frait = new Frait().type(UPDATED_TYPE).montant(UPDATED_MONTANT);
        return frait;
    }

    @BeforeEach
    public void initTest() {
        frait = createEntity(em);
    }

    @Test
    @Transactional
    void createFrait() throws Exception {
        int databaseSizeBeforeCreate = fraitRepository.findAll().size();
        // Create the Frait
        restFraitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frait)))
            .andExpect(status().isCreated());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeCreate + 1);
        Frait testFrait = fraitList.get(fraitList.size() - 1);
        assertThat(testFrait.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFrait.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void createFraitWithExistingId() throws Exception {
        // Create the Frait with an existing ID
        frait.setId(1L);

        int databaseSizeBeforeCreate = fraitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFraitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frait)))
            .andExpect(status().isBadRequest());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFraits() throws Exception {
        // Initialize the database
        fraitRepository.saveAndFlush(frait);

        // Get all the fraitList
        restFraitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frait.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    void getFrait() throws Exception {
        // Initialize the database
        fraitRepository.saveAndFlush(frait);

        // Get the frait
        restFraitMockMvc
            .perform(get(ENTITY_API_URL_ID, frait.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frait.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFrait() throws Exception {
        // Get the frait
        restFraitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFrait() throws Exception {
        // Initialize the database
        fraitRepository.saveAndFlush(frait);

        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();

        // Update the frait
        Frait updatedFrait = fraitRepository.findById(frait.getId()).get();
        // Disconnect from session so that the updates on updatedFrait are not directly saved in db
        em.detach(updatedFrait);
        updatedFrait.type(UPDATED_TYPE).montant(UPDATED_MONTANT);

        restFraitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFrait.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFrait))
            )
            .andExpect(status().isOk());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
        Frait testFrait = fraitList.get(fraitList.size() - 1);
        assertThat(testFrait.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFrait.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void putNonExistingFrait() throws Exception {
        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();
        frait.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frait.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrait() throws Exception {
        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();
        frait.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrait() throws Exception {
        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();
        frait.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frait)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFraitWithPatch() throws Exception {
        // Initialize the database
        fraitRepository.saveAndFlush(frait);

        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();

        // Update the frait using partial update
        Frait partialUpdatedFrait = new Frait();
        partialUpdatedFrait.setId(frait.getId());

        restFraitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrait))
            )
            .andExpect(status().isOk());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
        Frait testFrait = fraitList.get(fraitList.size() - 1);
        assertThat(testFrait.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFrait.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdateFraitWithPatch() throws Exception {
        // Initialize the database
        fraitRepository.saveAndFlush(frait);

        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();

        // Update the frait using partial update
        Frait partialUpdatedFrait = new Frait();
        partialUpdatedFrait.setId(frait.getId());

        partialUpdatedFrait.type(UPDATED_TYPE).montant(UPDATED_MONTANT);

        restFraitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrait))
            )
            .andExpect(status().isOk());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
        Frait testFrait = fraitList.get(fraitList.size() - 1);
        assertThat(testFrait.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFrait.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingFrait() throws Exception {
        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();
        frait.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, frait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrait() throws Exception {
        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();
        frait.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrait() throws Exception {
        int databaseSizeBeforeUpdate = fraitRepository.findAll().size();
        frait.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(frait)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frait in the database
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFrait() throws Exception {
        // Initialize the database
        fraitRepository.saveAndFlush(frait);

        int databaseSizeBeforeDelete = fraitRepository.findAll().size();

        // Delete the frait
        restFraitMockMvc
            .perform(delete(ENTITY_API_URL_ID, frait.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Frait> fraitList = fraitRepository.findAll();
        assertThat(fraitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
