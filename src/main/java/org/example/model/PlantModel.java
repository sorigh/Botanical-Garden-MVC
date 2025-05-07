package org.example.model;

import org.example.dto.PlantDTO;
import org.example.dto.PlantMapper;
import org.example.model.repository.PlantRepository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class PlantModel {
    private final PlantRepository repository;
    private final PropertyChangeSupport support;

    public PlantModel(PlantRepository repository) {
        this.repository = repository;
        this.support = new PropertyChangeSupport(this);
    }

    public void loadPlants() {
        List<Plant> plants = repository.getTableContent();
        List<PlantDTO> dtos = plants.stream()
                .map(PlantMapper::toDTO)
                .toList();
        support.firePropertyChange("plants", null, dtos);
    }

    public void addPlant(PlantDTO dto) {
        repository.insert(PlantMapper.toEntity(dto));
        loadPlants();
    }

    public void updatePlant(PlantDTO dto) {
        repository.update(PlantMapper.toEntity(dto));
        loadPlants();
    }

    public void deletePlant(PlantDTO dto) {
        repository.deleteById(PlantMapper.toEntity(dto).getPlant_id());
        loadPlants();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
