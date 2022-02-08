package com.ensa.web.rest;

import com.ensa.domain.Frait;
import com.ensa.repository.FraitRepository;
import com.ensa.service.FraitService;
import com.ensa.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ensa.domain.Frait}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FraitResource {

    private final Logger log = LoggerFactory.getLogger(FraitResource.class);

    @Autowired
    FraitService fraitService;

    /**
     * {@code POST  /fraits} : Create a new frait.
     *
     * @param frait the frait to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frait, or with status {@code 400 (Bad Request)} if the frait has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fraits")
    public int createFrait(@Valid @RequestBody Frait frait) throws URISyntaxException {
        log.debug("REST request to save Frait : {}", frait);
        return fraitService.createFrait(frait);
    }

    /**
     * {@code PUT  /fraits/:id} : Updates an existing frait.
     *
     * @param id    the id of the frait to save.
     * @param frait the frait to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frait,
     * or with status {@code 400 (Bad Request)} if the frait is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frait couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fraits/{id}")
    public int updateFrait(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Frait frait)
        throws URISyntaxException {
        log.debug("REST request to update Frait : {}, {}", id, frait);
      return   fraitService.updateFrait(id, frait);
    }

    /**
     * {@code PATCH  /fraits/:id} : Partial updates given fields of an existing frait, field will ignore if it is null
     *
     * @param id    the id of the frait to save.
     * @param frait the frait to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frait,
     * or with status {@code 400 (Bad Request)} if the frait is not valid,
     * or with status {@code 404 (Not Found)} if the frait is not found,
     * or with status {@code 500 (Internal Server Error)} if the frait couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fraits/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public int partialUpdateFrait(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Frait frait
    ) throws URISyntaxException {
        log.debug("REST request to partial update Frait partially : {}, {}", id, frait);
        return fraitService.partialUpdateFrait(id, frait);
    }

    /**
     * {@code GET  /fraits} : get all the fraits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fraits in body.
     */
    @GetMapping("/fraits")
    public List<Frait> getAllFraits() {
        log.debug("REST request to get all Fraits");
        return fraitService.findFraitAll();
    }

    /**
     * {@code GET  /fraits/:id} : get the "id" frait.
     *
     * @param id the id of the frait to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frait, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fraits/{id}")
    public ResponseEntity<Frait> getFrait(@PathVariable Long id) {
        log.debug("REST request to get Frait : {}", id);
        Optional<Frait> frait = fraitService.findFraitById(id);
        return ResponseUtil.wrapOrNotFound(frait);
    }

    /**
     * {@code DELETE  /fraits/:id} : delete the "id" frait.
     *
     * @param id the id of the frait to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fraits/{id}")
    public int deleteFrait(@PathVariable Long id) {
        log.debug("REST request to delete Frait : {}", id);
        return fraitService.deleteFrait(id);
    }
}
