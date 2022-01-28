package com.ensa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ensa.IntegrationTest;
import com.ensa.domain.ParametreGlobal;
import com.ensa.repository.ParametreGlobalRepository;
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
 * Integration tests for the {@link ParametreGlobalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParametreGlobalResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parametre-globals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParametreGlobalRepository parametreGlobalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParametreGlobalMockMvc;

    private ParametreGlobal parametreGlobal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParametreGlobal createEntity(EntityManager em) {
        ParametreGlobal parametreGlobal = new ParametreGlobal().key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return parametreGlobal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParametreGlobal createUpdatedEntity(EntityManager em) {
        ParametreGlobal parametreGlobal = new ParametreGlobal().key(UPDATED_KEY).value(UPDATED_VALUE);
        return parametreGlobal;
    }

    @BeforeEach
    public void initTest() {
        parametreGlobal = createEntity(em);
    }

    @Test
    @Transactional
    void createParametreGlobal() throws Exception {
        int databaseSizeBeforeCreate = parametreGlobalRepository.findAll().size();
        // Create the ParametreGlobal
        restParametreGlobalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametreGlobal))
            )
            .andExpect(status().isCreated());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeCreate + 1);
        ParametreGlobal testParametreGlobal = parametreGlobalList.get(parametreGlobalList.size() - 1);
        assertThat(testParametreGlobal.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testParametreGlobal.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createParametreGlobalWithExistingId() throws Exception {
        // Create the ParametreGlobal with an existing ID
        parametreGlobal.setId(1L);

        int databaseSizeBeforeCreate = parametreGlobalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParametreGlobalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametreGlobal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParametreGlobals() throws Exception {
        // Initialize the database
        parametreGlobalRepository.saveAndFlush(parametreGlobal);

        // Get all the parametreGlobalList
        restParametreGlobalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametreGlobal.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getParametreGlobal() throws Exception {
        // Initialize the database
        parametreGlobalRepository.saveAndFlush(parametreGlobal);

        // Get the parametreGlobal
        restParametreGlobalMockMvc
            .perform(get(ENTITY_API_URL_ID, parametreGlobal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parametreGlobal.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingParametreGlobal() throws Exception {
        // Get the parametreGlobal
        restParametreGlobalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParametreGlobal() throws Exception {
        // Initialize the database
        parametreGlobalRepository.saveAndFlush(parametreGlobal);

        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();

        // Update the parametreGlobal
        ParametreGlobal updatedParametreGlobal = parametreGlobalRepository.findById(parametreGlobal.getId()).get();
        // Disconnect from session so that the updates on updatedParametreGlobal are not directly saved in db
        em.detach(updatedParametreGlobal);
        updatedParametreGlobal.key(UPDATED_KEY).value(UPDATED_VALUE);

        restParametreGlobalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParametreGlobal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParametreGlobal))
            )
            .andExpect(status().isOk());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
        ParametreGlobal testParametreGlobal = parametreGlobalList.get(parametreGlobalList.size() - 1);
        assertThat(testParametreGlobal.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testParametreGlobal.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingParametreGlobal() throws Exception {
        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();
        parametreGlobal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametreGlobalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametreGlobal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreGlobal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParametreGlobal() throws Exception {
        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();
        parametreGlobal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreGlobalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametreGlobal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParametreGlobal() throws Exception {
        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();
        parametreGlobal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreGlobalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametreGlobal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParametreGlobalWithPatch() throws Exception {
        // Initialize the database
        parametreGlobalRepository.saveAndFlush(parametreGlobal);

        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();

        // Update the parametreGlobal using partial update
        ParametreGlobal partialUpdatedParametreGlobal = new ParametreGlobal();
        partialUpdatedParametreGlobal.setId(parametreGlobal.getId());

        partialUpdatedParametreGlobal.value(UPDATED_VALUE);

        restParametreGlobalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametreGlobal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametreGlobal))
            )
            .andExpect(status().isOk());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
        ParametreGlobal testParametreGlobal = parametreGlobalList.get(parametreGlobalList.size() - 1);
        assertThat(testParametreGlobal.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testParametreGlobal.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateParametreGlobalWithPatch() throws Exception {
        // Initialize the database
        parametreGlobalRepository.saveAndFlush(parametreGlobal);

        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();

        // Update the parametreGlobal using partial update
        ParametreGlobal partialUpdatedParametreGlobal = new ParametreGlobal();
        partialUpdatedParametreGlobal.setId(parametreGlobal.getId());

        partialUpdatedParametreGlobal.key(UPDATED_KEY).value(UPDATED_VALUE);

        restParametreGlobalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametreGlobal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametreGlobal))
            )
            .andExpect(status().isOk());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
        ParametreGlobal testParametreGlobal = parametreGlobalList.get(parametreGlobalList.size() - 1);
        assertThat(testParametreGlobal.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testParametreGlobal.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingParametreGlobal() throws Exception {
        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();
        parametreGlobal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametreGlobalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parametreGlobal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametreGlobal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParametreGlobal() throws Exception {
        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();
        parametreGlobal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreGlobalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametreGlobal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParametreGlobal() throws Exception {
        int databaseSizeBeforeUpdate = parametreGlobalRepository.findAll().size();
        parametreGlobal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreGlobalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametreGlobal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParametreGlobal in the database
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParametreGlobal() throws Exception {
        // Initialize the database
        parametreGlobalRepository.saveAndFlush(parametreGlobal);

        int databaseSizeBeforeDelete = parametreGlobalRepository.findAll().size();

        // Delete the parametreGlobal
        restParametreGlobalMockMvc
            .perform(delete(ENTITY_API_URL_ID, parametreGlobal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParametreGlobal> parametreGlobalList = parametreGlobalRepository.findAll();
        assertThat(parametreGlobalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
