package com.ensa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ensa.IntegrationTest;
import com.ensa.domain.Motif;
import com.ensa.repository.MotifRepository;
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
 * Integration tests for the {@link MotifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MotifResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/motifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MotifRepository motifRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMotifMockMvc;

    private Motif motif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Motif createEntity(EntityManager em) {
        Motif motif = new Motif().libelle(DEFAULT_LIBELLE);
        return motif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Motif createUpdatedEntity(EntityManager em) {
        Motif motif = new Motif().libelle(UPDATED_LIBELLE);
        return motif;
    }

    @BeforeEach
    public void initTest() {
        motif = createEntity(em);
    }

    @Test
    @Transactional
    void createMotif() throws Exception {
        int databaseSizeBeforeCreate = motifRepository.findAll().size();
        // Create the Motif
        restMotifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motif)))
            .andExpect(status().isCreated());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeCreate + 1);
        Motif testMotif = motifList.get(motifList.size() - 1);
        assertThat(testMotif.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createMotifWithExistingId() throws Exception {
        // Create the Motif with an existing ID
        motif.setId(1L);

        int databaseSizeBeforeCreate = motifRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motif)))
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMotifs() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        // Get all the motifList
        restMotifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motif.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getMotif() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        // Get the motif
        restMotifMockMvc
            .perform(get(ENTITY_API_URL_ID, motif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(motif.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingMotif() throws Exception {
        // Get the motif
        restMotifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMotif() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        int databaseSizeBeforeUpdate = motifRepository.findAll().size();

        // Update the motif
        Motif updatedMotif = motifRepository.findById(motif.getId()).get();
        // Disconnect from session so that the updates on updatedMotif are not directly saved in db
        em.detach(updatedMotif);
        updatedMotif.libelle(UPDATED_LIBELLE);

        restMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMotif.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMotif))
            )
            .andExpect(status().isOk());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
        Motif testMotif = motifList.get(motifList.size() - 1);
        assertThat(testMotif.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motif.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motif)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMotifWithPatch() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        int databaseSizeBeforeUpdate = motifRepository.findAll().size();

        // Update the motif using partial update
        Motif partialUpdatedMotif = new Motif();
        partialUpdatedMotif.setId(motif.getId());

        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotif))
            )
            .andExpect(status().isOk());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
        Motif testMotif = motifList.get(motifList.size() - 1);
        assertThat(testMotif.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateMotifWithPatch() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        int databaseSizeBeforeUpdate = motifRepository.findAll().size();

        // Update the motif using partial update
        Motif partialUpdatedMotif = new Motif();
        partialUpdatedMotif.setId(motif.getId());

        partialUpdatedMotif.libelle(UPDATED_LIBELLE);

        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotif))
            )
            .andExpect(status().isOk());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
        Motif testMotif = motifList.get(motifList.size() - 1);
        assertThat(testMotif.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, motif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMotif() throws Exception {
        int databaseSizeBeforeUpdate = motifRepository.findAll().size();
        motif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(motif)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Motif in the database
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMotif() throws Exception {
        // Initialize the database
        motifRepository.saveAndFlush(motif);

        int databaseSizeBeforeDelete = motifRepository.findAll().size();

        // Delete the motif
        restMotifMockMvc
            .perform(delete(ENTITY_API_URL_ID, motif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Motif> motifList = motifRepository.findAll();
        assertThat(motifList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
