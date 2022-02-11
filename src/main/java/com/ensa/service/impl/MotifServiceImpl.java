package com.ensa.service.impl;

import com.ensa.domain.Motif;
import com.ensa.repository.MotifRepository;
import com.ensa.service.MotifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotifServiceImpl implements MotifService {

    @Autowired
    MotifRepository repository;

    @Override
    public int createMotif(Motif motif) {
        Motif motifCheck = repository.findMotifByLibelle(motif.getLibelle());
        if (motifCheck == null) {
            repository.save(motif);
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int updateMotif(Long id, Motif motif) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.save(motif);
            return 1;
        }
    }

    @Override
    public int updateMotif(String libelle, Motif motif) {
        Motif motifToUpdate = repository.findMotifByLibelle(libelle);
        if (motifToUpdate == null) {
            return -1;
        } else {
            repository.save(motif);
            return 1;
        }
    }

    @Override
    public int partialUpdateMotif(Long id, Motif motif) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.findById(id)
                .map(existingMotif -> {
                    if (motif.getLibelle() != null) {
                        existingMotif.setLibelle(motif.getLibelle());
                    }

                    return existingMotif;
                })
                .map(repository::save);
            return 1;
        }
    }

    @Override
    public int partialUpdateMotif(String libelle, Motif motif) {
        Optional<Motif> motifToUpdate = repository.findOneMotifByLibelle(libelle);
        if (motifToUpdate.isEmpty()) {
            return -1;
        } else {
           motifToUpdate
                .map(existingMotif -> {
                    if (motif.getLibelle() != null) {
                        existingMotif.setLibelle(motif.getLibelle());
                    }

                    return existingMotif;
                })
                .map(repository::save);
            return 1;
        }
    }

    @Override
    public int deleteMotif(Long id) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.deleteById(id);
            return 1;
        }
    }

    @Override
    public int deleteMotif(String libelle) {
        Motif motifToDelete = repository.findMotifByLibelle(libelle);
        if (motifToDelete == null) {
            return -1;
        } else {
            repository.delete(motifToDelete);
            return 1;
        }
    }

    @Override
    public Optional<Motif> findMotifById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Motif> findMotifAll() {
        return repository.findAll();
    }
}
