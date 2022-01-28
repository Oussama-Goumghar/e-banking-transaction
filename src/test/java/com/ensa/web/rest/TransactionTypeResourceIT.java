package com.ensa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ensa.IntegrationTest;
import com.ensa.domain.TransactionType;
import com.ensa.repository.TransactionTypeRepository;
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
 * Integration tests for the {@link TransactionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_PLAFOND_TRANSACTION = 1D;
    private static final Double UPDATED_PLAFOND_TRANSACTION = 2D;

    private static final Double DEFAULT_PLAFOND_ANNUEL = 1D;
    private static final Double UPDATED_PLAFOND_ANNUEL = 2D;

    private static final String ENTITY_API_URL = "/api/transaction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionTypeMockMvc;

    private TransactionType transactionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createEntity(EntityManager em) {
        TransactionType transactionType = new TransactionType()
            .type(DEFAULT_TYPE)
            .plafondTransaction(DEFAULT_PLAFOND_TRANSACTION)
            .plafondAnnuel(DEFAULT_PLAFOND_ANNUEL);
        return transactionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createUpdatedEntity(EntityManager em) {
        TransactionType transactionType = new TransactionType()
            .type(UPDATED_TYPE)
            .plafondTransaction(UPDATED_PLAFOND_TRANSACTION)
            .plafondAnnuel(UPDATED_PLAFOND_ANNUEL);
        return transactionType;
    }

    @BeforeEach
    public void initTest() {
        transactionType = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionType() throws Exception {
        int databaseSizeBeforeCreate = transactionTypeRepository.findAll().size();
        // Create the TransactionType
        restTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTransactionType.getPlafondTransaction()).isEqualTo(DEFAULT_PLAFOND_TRANSACTION);
        assertThat(testTransactionType.getPlafondAnnuel()).isEqualTo(DEFAULT_PLAFOND_ANNUEL);
    }

    @Test
    @Transactional
    void createTransactionTypeWithExistingId() throws Exception {
        // Create the TransactionType with an existing ID
        transactionType.setId(1L);

        int databaseSizeBeforeCreate = transactionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransactionTypes() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].plafondTransaction").value(hasItem(DEFAULT_PLAFOND_TRANSACTION.doubleValue())))
            .andExpect(jsonPath("$.[*].plafondAnnuel").value(hasItem(DEFAULT_PLAFOND_ANNUEL.doubleValue())));
    }

    @Test
    @Transactional
    void getTransactionType() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        // Get the transactionType
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.plafondTransaction").value(DEFAULT_PLAFOND_TRANSACTION.doubleValue()))
            .andExpect(jsonPath("$.plafondAnnuel").value(DEFAULT_PLAFOND_ANNUEL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingTransactionType() throws Exception {
        // Get the transactionType
        restTransactionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionType() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();

        // Update the transactionType
        TransactionType updatedTransactionType = transactionTypeRepository.findById(transactionType.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionType are not directly saved in db
        em.detach(updatedTransactionType);
        updatedTransactionType.type(UPDATED_TYPE).plafondTransaction(UPDATED_PLAFOND_TRANSACTION).plafondAnnuel(UPDATED_PLAFOND_ANNUEL);

        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTransactionType.getPlafondTransaction()).isEqualTo(UPDATED_PLAFOND_TRANSACTION);
        assertThat(testTransactionType.getPlafondAnnuel()).isEqualTo(UPDATED_PLAFOND_ANNUEL);
    }

    @Test
    @Transactional
    void putNonExistingTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.plafondAnnuel(UPDATED_PLAFOND_ANNUEL);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTransactionType.getPlafondTransaction()).isEqualTo(DEFAULT_PLAFOND_TRANSACTION);
        assertThat(testTransactionType.getPlafondAnnuel()).isEqualTo(UPDATED_PLAFOND_ANNUEL);
    }

    @Test
    @Transactional
    void fullUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType
            .type(UPDATED_TYPE)
            .plafondTransaction(UPDATED_PLAFOND_TRANSACTION)
            .plafondAnnuel(UPDATED_PLAFOND_ANNUEL);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionType testTransactionType = transactionTypeList.get(transactionTypeList.size() - 1);
        assertThat(testTransactionType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTransactionType.getPlafondTransaction()).isEqualTo(UPDATED_PLAFOND_TRANSACTION);
        assertThat(testTransactionType.getPlafondAnnuel()).isEqualTo(UPDATED_PLAFOND_ANNUEL);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = transactionTypeRepository.findAll().size();
        transactionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionType() throws Exception {
        // Initialize the database
        transactionTypeRepository.saveAndFlush(transactionType);

        int databaseSizeBeforeDelete = transactionTypeRepository.findAll().size();

        // Delete the transactionType
        restTransactionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        assertThat(transactionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
