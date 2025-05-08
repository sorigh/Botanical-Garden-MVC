package org.example.model.viewmodel;

import org.example.controller.dto.SpecimenDTO;
import org.example.controller.dto.SpecimenMapper;
import org.example.model.Observable;
import org.example.model.Specimen;
import org.example.model.repository.SpecimenRepository;

import java.util.List;

public class SpecimenViewModel extends Observable {
    private final SpecimenRepository repository;
    private List<SpecimenDTO> currentSpecimens = List.of();

    public SpecimenViewModel(SpecimenRepository repository) {
        this.repository = repository;
    }

    public void loadSpecimens() {
        List<Specimen> specimens = repository.getTableContent();
        currentSpecimens = specimens.stream()
                .map(SpecimenMapper::toDTO)
                .toList();
        notifyObservers();
    }

    public void addSpecimen(SpecimenDTO dto) {
        repository.insert(SpecimenMapper.toEntity(dto));
        loadSpecimens(); // Already notifies
    }

    public void updateSpecimen(SpecimenDTO dto) {
        repository.update(SpecimenMapper.toEntity(dto));
        loadSpecimens(); // Already notifies
    }

    public void deleteSpecimen(SpecimenDTO dto) {
        repository.deleteById(SpecimenMapper.toEntity(dto).getSpecimen_id());
        loadSpecimens(); // Already notifies
    }

    public List<SpecimenDTO> getCurrentSpecimens() {
        return currentSpecimens;
    }
}
