package org.example.controller;

import org.example.model.Plant;
import org.example.model.repository.PlantRepository;
import org.example.view.PlantListView;

public class PlantListController {
    private PlantListView plantListView;
    private PlantRepository plantRepository;

    public PlantListController(PlantListView plantListView, PlantRepository plantRepository) {
        this.plantListView = plantListView;
        this.plantRepository = plantRepository;
        this.plantListView.setController(this);

        // Set up event listeners
        plantListView.getAddPlantButton().setOnAction(event -> addPlant());
        plantListView.getDeletePlantButton().setOnAction(event -> deletePlant());
    }

    private void addPlant() {
        // Logic to add a plant
        Plant plant = new Plant(1, "Flower", "Rosa", "specie", 1);
        plantRepository.insert(plant);
        updatePlantListView();
    }

    private void deletePlant() {
        Plant selectedPlant = plantListView.getPlantTable().getSelectionModel().getSelectedItem();
        if (selectedPlant != null) {
            plantRepository.getTableContent().remove(selectedPlant);
            updatePlantListView();
        }
    }

    public void updatePlantListView() {
        plantListView.getPlantTable().getItems().setAll(plantRepository.getTableContent());
    }
}
