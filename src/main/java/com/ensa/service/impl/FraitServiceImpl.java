package com.ensa.service.impl;

import com.ensa.domain.Frait;
import com.ensa.repository.FraitRepository;
import com.ensa.service.FraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FraitServiceImpl implements FraitService {
    @Autowired
    FraitRepository repository;

    @Override
    public int createFrait(Frait frait) {
        Frait fraitCheck = repository.findFraitByType(frait.getType());
        if (fraitCheck == null) {
            repository.save(frait);
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int updateFrait(Long id, Frait frait) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.save(frait);
            return 1;
        }
    }

    @Override
    public int updateFrait(String type, Frait frait) {
        Frait fraitToUpdate = repository.findFraitByType(type);
        if (fraitToUpdate == null) {
            return -1;
        } else {
            repository.save(fraitToUpdate);
            return 1;
        }
    }

    @Override
    public int partialUpdateFrait(Long id, Frait frait) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.findById(id)
                .map(existingFrait -> {
                    if (frait.getType() != null) {
                        existingFrait.setType(frait.getType());
                    }
                    if (frait.getMontant() != null) {
                        existingFrait.setMontant(frait.getMontant());
                    }

                    return existingFrait;
                }).map(repository::save);
            return 1;
        }
    }

    @Override
    public int partialUpdateFrait(String type, Frait frait) {
        Optional<Frait> fraitToUpdate = repository.findOneFraitByType(type);
        if (fraitToUpdate.isEmpty()) {
            return -1;
        } else {
            fraitToUpdate
                .map(existingFrait -> {
                    if (frait.getType() != null) {
                        existingFrait.setType(frait.getType());
                    }
                    if (frait.getMontant() != null) {
                        existingFrait.setMontant(frait.getMontant());
                    }

                    return existingFrait;
                }).map(repository::save);
            return 1;
        }
    }

    @Override
    public int deleteFraitById(Long id) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.deleteById(id);
            return 1;
        }
    }

    @Override
    public int deleteFraitByType(String type) {
        Frait fraitToDelete = repository.findFraitByType(type);
        if (fraitToDelete == null) {
            return -1;
        } else {
            repository.delete(fraitToDelete);
            return 1;
        }
    }

    @Override
    public Optional<Frait> findFraitById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Frait> findFraitAll() {
        return repository.findAll();
    }
}
