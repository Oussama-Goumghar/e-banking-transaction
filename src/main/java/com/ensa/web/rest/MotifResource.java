package com.ensa.web.rest;

import com.ensa.domain.Motif;
import com.ensa.repository.MotifRepository;
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
 * REST controller for managing {@link com.ensa.domain.Motif}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MotifResource {

    private final Logger log = LoggerFactory.getLogger(MotifResource.class);

    private static final String ENTITY_NAME = "transactionApiMotif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MotifRepository motifRepository;

    public MotifResource(MotifRepository motifRepository) {
        this.motifRepository = motifRepository;
    }

    /**
     * {@code POST  /motifs} : Create a new motif.
     *
     * @param motif the motif to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new motif, or with status {@code 400 (Bad Request)} if the motif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/motifs")
    public ResponseEntity<Motif> createMotif(@Valid @RequestBody Motif motif) throws URISyntaxException {
        log.debug("REST request to save Motif : {}", motif);
        if (motif.getId() != null) {
            throw new BadRequestAlertException("A new motif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Motif result = motifRepository.save(motif);
        return ResponseEntity
            .created(new URI("/api/motifs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /motifs/:id} : Updates an existing motif.
     *
     * @param id the id of the motif to save.
     * @param motif the motif to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motif,
     * or with status {@code 400 (Bad Request)} if the motif is not valid,
     * or with status {@code 500 (Internal Server Error)} if the motif couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/motifs/{id}")
    public ResponseEntity<Motif> updateMotif(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Motif motif)
        throws URISyntaxException {
        log.debug("REST request to update Motif : {}, {}", id, motif);
        if (motif.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motif.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Motif result = motifRepository.save(motif);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motif.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /motifs/:id} : Partial updates given fields of an existing motif, field will ignore if it is null
     *
     * @param id the id of the motif to save.
     * @param motif the motif to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motif,
     * or with status {@code 400 (Bad Request)} if the motif is not valid,
     * or with status {@code 404 (Not Found)} if the motif is not found,
     * or with status {@code 500 (Internal Server Error)} if the motif couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/motifs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Motif> partialUpdateMotif(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Motif motif
    ) throws URISyntaxException {
        log.debug("REST request to partial update Motif partially : {}, {}", id, motif);
        if (motif.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motif.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Motif> result = motifRepository
            .findById(motif.getId())
            .map(existingMotif -> {
                if (motif.getLibelle() != null) {
                    existingMotif.setLibelle(motif.getLibelle());
                }

                return existingMotif;
            })
            .map(motifRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motif.getId().toString())
        );
    }

    /**
     * {@code GET  /motifs} : get all the motifs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motifs in body.
     */
    @GetMapping("/motifs")
    public List<Motif> getAllMotifs() {
        log.debug("REST request to get all Motifs");
        return motifRepository.findAll();
    }

    /**
     * {@code GET  /motifs/:id} : get the "id" motif.
     *
     * @param id the id of the motif to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the motif, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/motifs/{id}")
    public ResponseEntity<Motif> getMotif(@PathVariable Long id) {
        log.debug("REST request to get Motif : {}", id);
        Optional<Motif> motif = motifRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(motif);
    }

    /**
     * {@code DELETE  /motifs/:id} : delete the "id" motif.
     *
     * @param id the id of the motif to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/motifs/{id}")
    public ResponseEntity<Void> deleteMotif(@PathVariable Long id) {
        log.debug("REST request to delete Motif : {}", id);
        motifRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
