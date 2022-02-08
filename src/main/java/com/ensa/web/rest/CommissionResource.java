package com.ensa.web.rest;

import com.ensa.domain.Commission;
import com.ensa.repository.CommissionRepository;
import com.ensa.service.CommissionService;
import com.ensa.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ensa.domain.Commission}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommissionResource {

    @Autowired
    CommissionService commissionService;

    private final Logger log = LoggerFactory.getLogger(CommissionResource.class);


    private static final String ENTITY_NAME = "transactionApiCommission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST  /commissions} : Create a new commission.
     *
     * @param commission the commission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commission, or with status {@code 400 (Bad Request)} if the commission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commissions")
    public int createCommission(@Valid @RequestBody Commission commission) throws URISyntaxException {
        log.debug("REST request to save Commission : {}", commission);
        if (commission.getId() != null) {
            throw new BadRequestAlertException("A new commission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return commissionService.createCommission(commission);
    }

    /**
     * {@code PUT  /commissions/:id} : Updates an existing commission.
     *
     * @param id         the id of the commission to save.
     * @param commission the commission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commission,
     * or with status {@code 400 (Bad Request)} if the commission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commissions/{id}")
    public int updateCommission(
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
        return commissionService.updateCommission(id, commission);
    }

    /**
     * {@code PATCH  /commissions/:id} : Partial updates given fields of an existing commission, field will ignore if it is null
     *
     * @param id         the id of the commission to save.
     * @param commission the commission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commission,
     * or with status {@code 400 (Bad Request)} if the commission is not valid,
     * or with status {@code 404 (Not Found)} if the commission is not found,
     * or with status {@code 500 (Internal Server Error)} if the commission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commissions/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public int partialUpdateCommission(
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
        return commissionService.partialUpdateCommission(id, commission);
    }

    /**
     * {@code GET  /commissions} : get all the commissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commissions in body.
     */
    @GetMapping("/commissions")
    public List<Commission> getAllCommissions() {
        log.debug("REST request to get all Commissions");
        return commissionService.findCommissionAll();
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
        Optional<Commission> commission = commissionService.findCommissionById(id);
        return ResponseUtil.wrapOrNotFound(commission);
    }

    /**
     * {@code DELETE  /commissions/:id} : delete the "id" commission.
     *
     * @param id the id of the commission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commissions/{id}")
    public int deleteCommission(@PathVariable Long id) {
        log.debug("REST request to delete Commission : {}", id);
        return commissionService.deleteCommission(id);
    }
}
