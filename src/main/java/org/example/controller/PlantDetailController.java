package org.example.controller;

import javafx.stage.Stage;
import org.example.model.Plant;
import org.example.view.PlantDetailView;

public class PlantDetailController {
    private PlantDetailView plantDetailView;
    private Plant plant;

    public PlantDetailController(PlantDetailView plantDetailView, Plant plant) {
        this.plantDetailView = plantDetailView;
        this.plant = plant;
        this.plantDetailView.updatePlantDetails(plant);

        // Event handler for closing the details view
        plantDetailView.getCloseButton().setOnAction(event -> closeDetailView());
    }

    private void closeDetailView() {
        // Logic to close the detail view
        Stage stage = (Stage) plantDetailView.getCloseButton().getScene().getWindow();
        stage.close();
    }
}
