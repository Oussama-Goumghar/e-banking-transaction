package com.ensa.web.rest;

import com.ensa.domain.Motif;
import com.ensa.repository.MotifRepository;
import com.ensa.service.MotifService;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    MotifService motifService;

    /**
     * {@code POST  /motifs} : Create a new motif.
     *
     * @param motif the motif to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new motif, or with status {@code 400 (Bad Request)} if the motif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/motifs")
    public int createMotif(@Valid @RequestBody Motif motif) throws URISyntaxException {
        log.debug("REST request to save Motif : {}", motif);
        return motifService.createMotif(motif);
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
    public int updateMotif(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Motif motif)
        throws URISyntaxException {
        log.debug("REST request to update Motif : {}, {}", id, motif);
        return motifService.updateMotif(id, motif);
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
    public int partialUpdateMotif(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Motif motif
    ) throws URISyntaxException {
        log.debug("REST request to partial update Motif partially : {}, {}", id, motif);
        return motifService.partialUpdateMotif(id, motif);
    }

    /**
     * {@code GET  /motifs} : get all the motifs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motifs in body.
     */
    @GetMapping("/motifs")
    public List<Motif> getAllMotifs() {
        log.debug("REST request to get all Motifs");
        return motifService.findMotifAll();
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
        Optional<Motif> motif = motifService.findMotifById(id);
        return ResponseUtil.wrapOrNotFound(motif);
    }

    /**
     * {@code DELETE  /motifs/:id} : delete the "id" motif.
     *
     * @param id the id of the motif to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/motifs/{id}")
    public int deleteMotif(@PathVariable Long id) {
        log.debug("REST request to delete Motif : {}", id);
        return motifService.deleteMotif(id);
    }
}
