package org.example.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.Plant;

public class PlantDetailView {
    private Label nameLabel;
    private Label typeLabel;
    private Label speciesLabel;
    private Label carnivorousLabel;
    private Button closeButton;

    public PlantDetailView() {
        this.nameLabel = new Label();
        this.typeLabel = new Label();
        this.speciesLabel = new Label();
        this.carnivorousLabel = new Label();
        this.closeButton = new Button("Close");

        // Layout
        VBox layout = new VBox(10, nameLabel, typeLabel, speciesLabel, carnivorousLabel, closeButton);
        Scene scene = new Scene(layout, 400, 400);

        Stage stage = new Stage();
        stage.setTitle("Plant Detail");
        stage.setScene(scene);
        stage.show();
    }

    public void updatePlantDetails(Plant plant) {
        nameLabel.setText("Name: " + plant.getName());
        typeLabel.setText("Type: " + plant.getType());
        speciesLabel.setText("Species: " + plant.getSpecies());
        carnivorousLabel.setText("Carnivorous: " + (plant.getCarnivore() == 1 ? "Yes" : "No"));
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
