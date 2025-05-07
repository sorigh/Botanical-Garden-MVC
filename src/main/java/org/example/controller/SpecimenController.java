package org.example.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import org.example.controller.dto.SpecimenDTO;
import org.example.model.viewmodel.SpecimenViewModel;
import org.example.view.SpecimenView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SpecimenController implements PropertyChangeListener {
    private final SpecimenViewModel model;
    private final SpecimenView view;
    private Locale currentLocale;
    private ResourceBundle bundle;

    public SpecimenController(SpecimenViewModel model, SpecimenView view, Locale locale) {
        this.model = model;
        this.view = view;
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("lang.messages", currentLocale);

        model.addPropertyChangeListener(this);
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

        // Selection handling
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                view.locationField.setText(newVal.getLocation());
                view.imageUrlField.setText(newVal.getImageUrl());
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("specimens".equals(evt.getPropertyName())) {
            List<SpecimenDTO> updated = (List<SpecimenDTO>) evt.getNewValue();
            view.specimenTable.setItems(FXCollections.observableArrayList(updated));
            view.messageLabel.setText(bundle.getString("message.refreshed"));
        }
    }

    private SpecimenDTO getDTOFromFields() {
        return new SpecimenDTO(0,
                Integer.parseInt(view.plantIdColumn.getText()),
                view.locationField.getText(),
                view.imageUrlField.getText());
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
