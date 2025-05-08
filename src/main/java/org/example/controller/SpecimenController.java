package org.example.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.controller.dto.SpecimenDTO;
import org.example.model.Observer;
import org.example.model.viewmodel.SpecimenViewModel;
import org.example.view.SpecimenView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SpecimenController {
    private final SpecimenViewModel model;
    private final SpecimenView view;
    private Locale currentLocale;
    private ResourceBundle bundle;

    public SpecimenController(SpecimenViewModel model, SpecimenView view, Locale locale) {
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
        TableView<SpecimenDTO> table = view.specimenTable;

        view.idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("specimen_id"));
        view.locationColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("location"));
        view.imageUrlColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("imageUrl"));
        view.plantIdColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("plant_id"));

        // Image column cell factory to display images as actual images
        view.imageUrlColumn.setCellFactory(column -> new TableCell<SpecimenDTO, String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        File file = new File(imageUrl);
                        Image image = new Image(file.toURI().toString(), 100, 100, true, true);
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);  // In case of an error, display nothing
                    }
                }
            }
        });

        // Selection handling
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                view.locationField.setText(newVal.getLocation());
                view.imageUrlField.setText(newVal.getImageUrl());
                view.plantIdField.setText(String.valueOf(newVal.getPlant_id()));  // Update plant ID field
            }
        });

        updateLanguage(); // Set initial labels
    }

    private void setupEventHandlers() {
        view.addButton.setOnAction(e -> {
            model.addSpecimen(getDTOFromFields());
            clearFields();
        });

        view.updateButton.setOnAction(e -> {
            SpecimenDTO dto = getDTOFromFieldsWithId();
            if (dto != null) {
                model.updateSpecimen(dto);
                clearFields();
            }
        });

        view.deleteButton.setOnAction(e -> {
            SpecimenDTO dto = getDTOFromFieldsWithId();
            if (dto != null) {
                model.deleteSpecimen(dto);
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
        model.loadSpecimens(); // This should fire a property change to update table
    }

    private SpecimenDTO getDTOFromFields() {
        try {
            return new SpecimenDTO(0,
                    Integer.parseInt(view.plantIdField.getText()), // Use the correct input field for plant ID
                    view.locationField.getText(),
                    view.imageUrlField.getText());
        } catch (NumberFormatException e) {
            // Handle the case where the plantId input is not a valid number
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid Plant ID");
            alert.setContentText("Please enter a valid integer for Plant ID.");
            alert.showAndWait();
            return null;
        }
    }

    private SpecimenDTO getDTOFromFieldsWithId() {
        SpecimenDTO selected = view.specimenTable.getSelectionModel().getSelectedItem();
        if (selected == null) return null;
        return new SpecimenDTO(
                selected.getSpecimen_id(),
                selected.getPlant_id(),
                view.locationField.getText(),
                view.imageUrlField.getText());
    }

    private void clearFields() {
        view.locationField.clear();
        view.imageUrlField.clear();
    }

    private void updateLanguage() {
        view.locationLabel.setText(bundle.getString("label.location"));
        view.imageUrlLabel.setText(bundle.getString("label.imageUrl"));

        view.idColumn.setText(bundle.getString("column.id"));
        view.locationColumn.setText(bundle.getString("column.location"));
        view.imageUrlColumn.setText(bundle.getString("column.imageUrl"));
        view.plantIdColumn.setText(bundle.getString("column.plantId"));

        view.addButton.setText(bundle.getString("button.add"));
        view.updateButton.setText(bundle.getString("button.update"));
        view.deleteButton.setText(bundle.getString("button.delete"));
        view.clearButton.setText(bundle.getString("button.clear"));
    }
}
