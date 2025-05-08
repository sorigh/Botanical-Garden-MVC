package org.example.model.viewmodel;

import org.example.controller.dto.PlantDTO;
import org.example.controller.dto.PlantMapper;
import org.example.model.Observable;
import org.example.model.Plant;
import org.example.model.repository.PlantRepository;


import java.util.List;

public class PlantViewModel extends Observable {
    private final PlantRepository repository;
    private List<PlantDTO> currentPlants;

    public PlantViewModel(PlantRepository repository) {
        this.repository = repository;
    }

    public void loadPlants() {
        List<Plant> plants = repository.getTableContent();
        currentPlants = plants.stream()
                .map(PlantMapper::toDTO)
                .toList();
        notifyObservers();
    }

    public List<PlantDTO> getCurrentPlants() {
        return currentPlants;
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
}