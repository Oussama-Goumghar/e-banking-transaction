package com.ensa.web.rest;

import com.ensa.domain.Commission;
import com.ensa.repository.CommissionRepository;
import com.ensa.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ensa.domain.Commission}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommissionResource {

    private final Logger log = LoggerFactory.getLogger(CommissionResource.class);

    private static final String ENTITY_NAME = "transactionApiCommission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommissionRepository commissionRepository;

    public CommissionResource(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }

    /**
     * {@code POST  /commissions} : Create a new commission.
     *
     * @param commission the commission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commission, or with status {@code 400 (Bad Request)} if the commission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commissions")
    public ResponseEntity<Commission> createCommission(@Valid @RequestBody Commission commission) throws URISyntaxException {
        log.debug("REST request to save Commission : {}", commission);
        if (commission.getId() != null) {
            throw new BadRequestAlertException("A new commission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Commission result = commissionRepository.save(commission);
        return ResponseEntity
            .created(new URI("/api/commissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commissions/:id} : Updates an existing commission.
     *
     * @param id the id of the commission to save.
     * @param commission the commission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commission,
     * or with status {@code 400 (Bad Request)} if the commission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commissions/{id}")
    public ResponseEntity<Commission> updateCommission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Commission commission
    ) throws URISyntaxException {
        log.debug("REST request to update Commission : {}, {}", id, commission);
        if (commission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Commission result = commissionRepository.save(commission);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commission.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /commissions/:id} : Partial updates given fields of an existing commission, field will ignore if it is null
     *
     * @param id the id of the commission to save.
     * @param commission the commission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commission,
     * or with status {@code 400 (Bad Request)} if the commission is not valid,
     * or with status {@code 404 (Not Found)} if the commission is not found,
     * or with status {@code 500 (Internal Server Error)} if the commission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Commission> partialUpdateCommission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Commission commission
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commission partially : {}, {}", id, commission);
        if (commission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Commission> result = commissionRepository
            .findById(commission.getId())
            .map(existingCommission -> {
                if (commission.getIdAgent() != null) {
                    existingCommission.setIdAgent(commission.getIdAgent());
                }
                if (commission.getDateRetrait() != null) {
                    existingCommission.setDateRetrait(commission.getDateRetrait());
                }
                if (commission.getValue() != null) {
                    existingCommission.setValue(commission.getValue());
                }

                return existingCommission;
            })
            .map(commissionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commission.getId().toString())
        );
    }

    /**
     * {@code GET  /commissions} : get all the commissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commissions in body.
     */
    @GetMapping("/commissions")
    public List<Commission> getAllCommissions() {
        log.debug("REST request to get all Commissions");
        return commissionRepository.findAll();
    }

    /**
     * {@code GET  /commissions/:id} : get the "id" commission.
     *
     * @param id the id of the commission to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commission, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commissions/{id}")
    public ResponseEntity<Commission> getCommission(@PathVariable Long id) {
        log.debug("REST request to get Commission : {}", id);
        Optional<Commission> commission = commissionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commission);
    }

    /**
     * {@code DELETE  /commissions/:id} : delete the "id" commission.
     *
     * @param id the id of the commission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commissions/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable Long id) {
        log.debug("REST request to delete Commission : {}", id);
        commissionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
