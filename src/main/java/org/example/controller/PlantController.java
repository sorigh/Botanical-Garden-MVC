package org.example.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import org.example.controller.dto.PlantDTO;
import org.example.model.viewmodel.PlantViewModel;
import org.example.view.PlantView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PlantController {
    private final PlantViewModel model;
    private final PlantView view;
    private Locale currentLocale;
    private ResourceBundle bundle;

    public PlantController(PlantViewModel model, PlantView view, Locale locale) {
        this.model = model;
        this.view = view;
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("lang.messages", currentLocale);

        model.addObserver(view);
        setupBindings();
        setupEventHandlers();
        load();
    }

    private void setupBindings() {
        TableView<PlantDTO> table = view.plantTable;

        view.idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("plant_id"));
        view.nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        view.typeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("type"));
        view.speciesColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("species"));
        view.carnivoreColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("carnivore"));

        // Selection handling
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                view.nameField.setText(newVal.getName());
                view.typeField.setText(newVal.getType());
                view.speciesField.setText(newVal.getSpecies());
                view.carnivoreField.setText(String.valueOf(newVal.getCarnivore()));
            }
        });

        updateLanguage(); // Set initial labels
    }

    private void setupEventHandlers() {
        view.addButton.setOnAction(e -> {
            model.addPlant(getDTOFromFields());
            clearFields();
        });

        view.updateButton.setOnAction(e -> {
            PlantDTO dto = getDTOFromFieldsWithId();
            if (dto != null) {
                model.updatePlant(dto);
                clearFields();
            }
        });

        view.deleteButton.setOnAction(e -> {
            PlantDTO dto = getDTOFromFieldsWithId();
            if (dto != null) {
                model.deletePlant(dto);
                clearFields();
            }
        });

        view.clearButton.setOnAction(e -> clearFields());

        ChangeListener<Toggle> languageListener = (obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selected = (RadioButton) newToggle;
                switch (selected.getText()) {
                    case "English" -> currentLocale = Locale.ENGLISH;
                    case "Francais" -> currentLocale = Locale.FRENCH;
                    case "Romana" -> currentLocale = new Locale("ro");
                }
                bundle = ResourceBundle.getBundle("lang.messages", currentLocale);
                updateLanguage();
            }
        };

        view.languageToggleGroup.selectedToggleProperty().addListener(languageListener);


    }

    public void load() {
        model.loadPlants(); // This should fire a property change to update table
    }

    private PlantDTO getDTOFromFields() {
        return new PlantDTO(0,
                view.nameField.getText(),
                view.speciesField.getText(),
                view.typeField.getText(),
                Integer.parseInt(view.carnivoreField.getText()));
    }

    private PlantDTO getDTOFromFieldsWithId() {
        PlantDTO selected = view.plantTable.getSelectionModel().getSelectedItem();
        if (selected == null) return null;
        return new PlantDTO(selected.getPlant_id(),
                view.nameField.getText(),
                view.speciesField.getText(),
                view.typeField.getText(),
                Integer.parseInt(view.carnivoreField.getText()));
    }

    private void clearFields() {
        view.nameField.clear();
        view.typeField.clear();
        view.speciesField.clear();
        view.carnivoreField.clear();
    }

    private void updateLanguage() {
        view.nameLabel.setText(bundle.getString("label.name"));
        view.typeLabel.setText(bundle.getString("label.type"));
        view.speciesLabel.setText(bundle.getString("label.species"));
        view.carnivoreLabel.setText(bundle.getString("label.carnivore"));

        view.idColumn.setText(bundle.getString("column.id"));
        view.nameColumn.setText(bundle.getString("column.name"));
        view.typeColumn.setText(bundle.getString("column.type"));
        view.speciesColumn.setText(bundle.getString("column.species"));
        view.carnivoreColumn.setText(bundle.getString("column.carnivore"));

        view.addButton.setText(bundle.getString("button.add"));
        view.updateButton.setText(bundle.getString("button.update"));
        view.deleteButton.setText(bundle.getString("button.delete"));
        view.clearButton.setText(bundle.getString("button.clear"));
    }
}
