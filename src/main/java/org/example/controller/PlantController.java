package org.example.controller;

import org.example.dto.PlantDTO;
import org.example.model.PlantModel;

public class PlantController {
    private final PlantModel model;

    public PlantController(PlantModel model) {
        this.model = model;
    }

    public void load() {
        model.loadPlants();
    }

    public void add(PlantDTO dto) {
        model.addPlant(dto);
    }

    public void update(PlantDTO dto) {
        model.updatePlant(dto);
    }

    public void delete(PlantDTO dto) {
        model.deletePlant(dto);
    }
}
