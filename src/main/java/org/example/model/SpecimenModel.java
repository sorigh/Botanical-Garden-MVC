package org.example.model;

import org.example.dto.SpecimenDTO;
import org.example.dto.SpecimenMapper;
import org.example.model.repository.SpecimenRepository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class SpecimenModel {
    private final SpecimenRepository repository;
    private final PropertyChangeSupport support;

    public SpecimenModel(SpecimenRepository repository) {
        this.repository = repository;
        this.support = new PropertyChangeSupport(this);
    }

    public void loadSpecimens() {
        List<Specimen> specimens = repository.getTableContent();
        List<SpecimenDTO> dtos = specimens.stream()
                .map(SpecimenMapper::toDTO)
                .toList();
        support.firePropertyChange("specimens", null, dtos);
    }

    public void addSpecimen(SpecimenDTO dto) {
        repository.insert(SpecimenMapper.toEntity(dto));
        loadSpecimens();
    }

    public void updateSpecimen(SpecimenDTO dto) {
        repository.update(SpecimenMapper.toEntity(dto));
        loadSpecimens();
    }

    public void deleteSpecimen(SpecimenDTO dto) {
        repository.deleteById(SpecimenMapper.toEntity(dto).getSpecimen_id());
        loadSpecimens();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}