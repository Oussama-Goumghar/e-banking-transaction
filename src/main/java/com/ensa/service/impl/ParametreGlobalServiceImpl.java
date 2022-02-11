package com.ensa.service.impl;

import com.ensa.domain.ParametreGlobal;
import com.ensa.repository.ParametreGlobalRepository;
import com.ensa.service.ParametreGlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParametreGlobalServiceImpl implements ParametreGlobalService {

    @Autowired
    ParametreGlobalRepository repository;

    @Override
    public int createParametreGlobal(ParametreGlobal parametreGlobal) {
        ParametreGlobal parametreGlobalCheck = repository.findParametreGlobalByKey(parametreGlobal.getKey());
        if (parametreGlobalCheck == null) {
            repository.save(parametreGlobal);
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int updateParametreGlobal(Long id, ParametreGlobal parametreGlobal) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.save(parametreGlobal);
            return 1;
        }
    }

    @Override
    public int updateParametreGlobal(String key, ParametreGlobal parametreGlobal) {
        ParametreGlobal parametreGlobalToUpdate = repository.findParametreGlobalByKey(key);
        if (parametreGlobalToUpdate == null) {
            return -1;
        } else {
            repository.save(parametreGlobal);
            return 1;
        }
    }

    @Override
    public int partialUpdateParametreGlobal(Long id, ParametreGlobal parametreGlobal) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.findById(id)
                .map(existingParametreGlobal -> {
                    if (parametreGlobal.getKey() != null) {
                        existingParametreGlobal.setKey(parametreGlobal.getKey());
                    }
                    if (parametreGlobal.getValue() != null) {
                        existingParametreGlobal.setValue(parametreGlobal.getValue());
                    }

                    return existingParametreGlobal;
                })
                .map(repository::save);
            return 1;
        }
    }

    @Override
    public int partialUpdateParametreGlobal(String key, ParametreGlobal parametreGlobal) {
        Optional<ParametreGlobal> parametreGlobalToUpdate = repository.findOneParametreGlobalByKey(key);
        if (parametreGlobalToUpdate.isEmpty()) {
            return -1;
        } else {
            parametreGlobalToUpdate .map(existingParametreGlobal -> {
                    if (parametreGlobal.getKey() != null) {
                        existingParametreGlobal.setKey(parametreGlobal.getKey());
                    }
                    if (parametreGlobal.getValue() != null) {
                        existingParametreGlobal.setValue(parametreGlobal.getValue());
                    }

                    return existingParametreGlobal;
                })
                .map(repository::save);
            return 1;
        }
    }

    @Override
    public int deleteParametreGlobal(Long id) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.deleteById(id);
            return 1;
        }
    }

    @Override
    public int deleteParametreGlobal(String key) {
        ParametreGlobal parametreGlobal = repository.findParametreGlobalByKey(key);
        if (parametreGlobal == null) {
            return -1;
        } else {
            repository.delete(parametreGlobal);
            return 1;
        }
    }

    @Override
    public Optional<ParametreGlobal> findParametreGlobalById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<ParametreGlobal> findParametreGlobalAll() {
        return repository.findAll();
    }
}
