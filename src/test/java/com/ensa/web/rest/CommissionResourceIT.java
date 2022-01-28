package com.ensa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ensa.IntegrationTest;
import com.ensa.domain.Commission;
import com.ensa.repository.CommissionRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CommissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommissionResourceIT {

    private static final Long DEFAULT_ID_AGENT = 1L;
    private static final Long UPDATED_ID_AGENT = 2L;

    private static final LocalDate DEFAULT_DATE_RETRAIT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RETRAIT = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/commissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommissionMockMvc;

    private Commission commission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commission createEntity(EntityManager em) {
        Commission commission = new Commission().idAgent(DEFAULT_ID_AGENT).dateRetrait(DEFAULT_DATE_RETRAIT).value(DEFAULT_VALUE);
        return commission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commission createUpdatedEntity(EntityManager em) {
        Commission commission = new Commission().idAgent(UPDATED_ID_AGENT).dateRetrait(UPDATED_DATE_RETRAIT).value(UPDATED_VALUE);
        return commission;
    }

    @BeforeEach
    public void initTest() {
        commission = createEntity(em);
    }

    @Test
    @Transactional
    void createCommission() throws Exception {
        int databaseSizeBeforeCreate = commissionRepository.findAll().size();
        // Create the Commission
        restCommissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commission)))
            .andExpect(status().isCreated());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeCreate + 1);
        Commission testCommission = commissionList.get(commissionList.size() - 1);
        assertThat(testCommission.getIdAgent()).isEqualTo(DEFAULT_ID_AGENT);
        assertThat(testCommission.getDateRetrait()).isEqualTo(DEFAULT_DATE_RETRAIT);
        assertThat(testCommission.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createCommissionWithExistingId() throws Exception {
        // Create the Commission with an existing ID
        commission.setId(1L);

        int databaseSizeBeforeCreate = commissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commission)))
            .andExpect(status().isBadRequest());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommissions() throws Exception {
        // Initialize the database
        commissionRepository.saveAndFlush(commission);

        // Get all the commissionList
        restCommissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commission.getId().intValue())))
            .andExpect(jsonPath("$.[*].idAgent").value(hasItem(DEFAULT_ID_AGENT.intValue())))
            .andExpect(jsonPath("$.[*].dateRetrait").value(hasItem(DEFAULT_DATE_RETRAIT.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    void getCommission() throws Exception {
        // Initialize the database
        commissionRepository.saveAndFlush(commission);

        // Get the commission
        restCommissionMockMvc
            .perform(get(ENTITY_API_URL_ID, commission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commission.getId().intValue()))
            .andExpect(jsonPath("$.idAgent").value(DEFAULT_ID_AGENT.intValue()))
            .andExpect(jsonPath("$.dateRetrait").value(DEFAULT_DATE_RETRAIT.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCommission() throws Exception {
        // Get the commission
        restCommissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommission() throws Exception {
        // Initialize the database
        commissionRepository.saveAndFlush(commission);

        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();

        // Update the commission
        Commission updatedCommission = commissionRepository.findById(commission.getId()).get();
        // Disconnect from session so that the updates on updatedCommission are not directly saved in db
        em.detach(updatedCommission);
        updatedCommission.idAgent(UPDATED_ID_AGENT).dateRetrait(UPDATED_DATE_RETRAIT).value(UPDATED_VALUE);

        restCommissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommission))
            )
            .andExpect(status().isOk());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
        Commission testCommission = commissionList.get(commissionList.size() - 1);
        assertThat(testCommission.getIdAgent()).isEqualTo(UPDATED_ID_AGENT);
        assertThat(testCommission.getDateRetrait()).isEqualTo(UPDATED_DATE_RETRAIT);
        assertThat(testCommission.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingCommission() throws Exception {
        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();
        commission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommission() throws Exception {
        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();
        commission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommission() throws Exception {
        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();
        commission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commission)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommissionWithPatch() throws Exception {
        // Initialize the database
        commissionRepository.saveAndFlush(commission);

        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();

        // Update the commission using partial update
        Commission partialUpdatedCommission = new Commission();
        partialUpdatedCommission.setId(commission.getId());

        partialUpdatedCommission.dateRetrait(UPDATED_DATE_RETRAIT).value(UPDATED_VALUE);

        restCommissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommission))
            )
            .andExpect(status().isOk());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
        Commission testCommission = commissionList.get(commissionList.size() - 1);
        assertThat(testCommission.getIdAgent()).isEqualTo(DEFAULT_ID_AGENT);
        assertThat(testCommission.getDateRetrait()).isEqualTo(UPDATED_DATE_RETRAIT);
        assertThat(testCommission.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateCommissionWithPatch() throws Exception {
        // Initialize the database
        commissionRepository.saveAndFlush(commission);

        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();

        // Update the commission using partial update
        Commission partialUpdatedCommission = new Commission();
        partialUpdatedCommission.setId(commission.getId());

        partialUpdatedCommission.idAgent(UPDATED_ID_AGENT).dateRetrait(UPDATED_DATE_RETRAIT).value(UPDATED_VALUE);

        restCommissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommission))
            )
            .andExpect(status().isOk());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
        Commission testCommission = commissionList.get(commissionList.size() - 1);
        assertThat(testCommission.getIdAgent()).isEqualTo(UPDATED_ID_AGENT);
        assertThat(testCommission.getDateRetrait()).isEqualTo(UPDATED_DATE_RETRAIT);
        assertThat(testCommission.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingCommission() throws Exception {
        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();
        commission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommission() throws Exception {
        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();
        commission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commission))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommission() throws Exception {
        int databaseSizeBeforeUpdate = commissionRepository.findAll().size();
        commission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commission))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commission in the database
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommission() throws Exception {
        // Initialize the database
        commissionRepository.saveAndFlush(commission);

        int databaseSizeBeforeDelete = commissionRepository.findAll().size();

        // Delete the commission
        restCommissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, commission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commission> commissionList = commissionRepository.findAll();
        assertThat(commissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
