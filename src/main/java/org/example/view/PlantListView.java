package org.example.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.controller.PlantListController;
import org.example.model.Plant;

public class PlantListView {
    private TableView<Plant> plantTable;
    private Button addPlantButton;
    private Button deletePlantButton;
    private PlantListController controller;

    public PlantListView(Stage stage) {
        plantTable = new TableView<>();

        TableColumn<Plant, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));

        TableColumn<Plant, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Plant, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Plant, String> speciesCol = new TableColumn<>("Species");
        speciesCol.setCellValueFactory(new PropertyValueFactory<>("species"));

        TableColumn<Plant, Integer> carnivoreColumn = new TableColumn<>("Carnivore");
        carnivoreColumn.setCellValueFactory(new PropertyValueFactory<>("carnivore"));


        plantTable.getColumns().addAll(idColumn, nameCol, typeCol, speciesCol, carnivoreColumn);

        addPlantButton = new Button("Add Plant");
        deletePlantButton = new Button("Delete Plant");

        VBox layout = new VBox(10, plantTable, addPlantButton, deletePlantButton);
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Botanical Garden - Plant Management");
        stage.show();
    }

    public void setController(PlantListController controller) {
        this.controller = controller;
    }

    public TableView<Plant> getPlantTable() {
        return plantTable;
    }

    public Button getAddPlantButton() {
        return addPlantButton;
    }

    public Button getDeletePlantButton() {
        return deletePlantButton;
    }
}
