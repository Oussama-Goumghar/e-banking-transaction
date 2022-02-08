package com.ensa.web.rest;

import com.ensa.domain.ParametreGlobal;
import com.ensa.repository.ParametreGlobalRepository;
import com.ensa.service.ParametreGlobalService;
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
 * REST controller for managing {@link com.ensa.domain.ParametreGlobal}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ParametreGlobalResource {

    private final Logger log = LoggerFactory.getLogger(ParametreGlobalResource.class);

    @Autowired
    ParametreGlobalService parametreGlobalService;
    /**
     * {@code POST  /parametre-globals} : Create a new parametreGlobal.
     *
     * @param parametreGlobal the parametreGlobal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametreGlobal, or with status {@code 400 (Bad Request)} if the parametreGlobal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parametre-globals")
    public int createParametreGlobal(@Valid @RequestBody ParametreGlobal parametreGlobal)
        throws URISyntaxException {
        log.debug("REST request to save ParametreGlobal : {}", parametreGlobal);
        return parametreGlobalService.createParametreGlobal(parametreGlobal);
    }

    /**
     * {@code PUT  /parametre-globals/:id} : Updates an existing parametreGlobal.
     *
     * @param id the id of the parametreGlobal to save.
     * @param parametreGlobal the parametreGlobal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametreGlobal,
     * or with status {@code 400 (Bad Request)} if the parametreGlobal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametreGlobal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parametre-globals/{id}")
    public int updateParametreGlobal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParametreGlobal parametreGlobal
    ) throws URISyntaxException {
        log.debug("REST request to update ParametreGlobal : {}, {}", id, parametreGlobal);
        return parametreGlobalService.updateParametreGlobal(id, parametreGlobal);
    }

    /**
     * {@code PATCH  /parametre-globals/:id} : Partial updates given fields of an existing parametreGlobal, field will ignore if it is null
     *
     * @param id the id of the parametreGlobal to save.
     * @param parametreGlobal the parametreGlobal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametreGlobal,
     * or with status {@code 400 (Bad Request)} if the parametreGlobal is not valid,
     * or with status {@code 404 (Not Found)} if the parametreGlobal is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametreGlobal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parametre-globals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public int partialUpdateParametreGlobal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParametreGlobal parametreGlobal
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParametreGlobal partially : {}, {}", id, parametreGlobal);
        return parametreGlobalService.partialUpdateParametreGlobal(id, parametreGlobal);
    }

    /**
     * {@code GET  /parametre-globals} : get all the parametreGlobals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametreGlobals in body.
     */
    @GetMapping("/parametre-globals")
    public List<ParametreGlobal> getAllParametreGlobals() {
        log.debug("REST request to get all ParametreGlobals");
        return parametreGlobalService.findParametreGlobalAll();
    }

    /**
     * {@code GET  /parametre-globals/:id} : get the "id" parametreGlobal.
     *
     * @param id the id of the parametreGlobal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametreGlobal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parametre-globals/{id}")
    public ResponseEntity<ParametreGlobal> getParametreGlobal(@PathVariable Long id) {
        log.debug("REST request to get ParametreGlobal : {}", id);
        Optional<ParametreGlobal> parametreGlobal = parametreGlobalService.findParametreGlobalById(id);
        return ResponseUtil.wrapOrNotFound(parametreGlobal);
    }

    /**
     * {@code DELETE  /parametre-globals/:id} : delete the "id" parametreGlobal.
     *
     * @param id the id of the parametreGlobal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parametre-globals/{id}")
    public int deleteParametreGlobal(@PathVariable Long id) {
        log.debug("REST request to delete ParametreGlobal : {}", id);
        return parametreGlobalService.deleteParametreGlobal(id);
    }
}
