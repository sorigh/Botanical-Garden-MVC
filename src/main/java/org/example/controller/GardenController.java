package org.example.controller;

import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.RadioButton;
import org.example.controller.dto.PlantDTO;
import org.example.controller.dto.SpecimenDTO;
import org.example.model.viewmodel.GardenViewModel;
import org.example.view.GardenView;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GardenController {
    private final GardenViewModel viewModel;
    private final GardenView view;
    private Locale currentLocale;
    private ResourceBundle bundle;

    public GardenController(GardenViewModel viewModel, GardenView view, Locale locale) {
        this.viewModel = viewModel;
        this.view = view;
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("lang.messages", currentLocale);

        setupBindings();
        setupEventHandlers();
        loadData();
        setupLanguageChangeListener();
        updateLanguage(); // initialize with proper language
    }

    private void setupBindings() {
        // Bind plant table columns
        view.idColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        view.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        view.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        view.speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        view.carnivoreColumn.setCellValueFactory(new PropertyValueFactory<>("carnivore"));
        view.carnivoreColumn.setCellFactory(column -> new TableCell<PlantDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item == 1 ? bundle.getString("yes") : bundle.getString("no"));
                }
            }
        });

        // Bind specimen table columns
        view.specimenIdColumn.setCellValueFactory(new PropertyValueFactory<>("specimen_id"));
        view.plantIdColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        view.locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        view.imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        view.imageColumn.setCellFactory(column -> new TableCell<SpecimenDTO, String>() {
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
                        setGraphic(null);
                    }
                }
            }
        });

        // Populate filter box
        view.filterTypeBox.setItems(FXCollections.observableArrayList(viewModel.getAvailableTypes()));
    }

    private void setupEventHandlers() {
        view.filterButton.setOnAction(e -> {
            String selectedType = view.filterTypeBox.getValue();
            boolean carnivorousOnly = view.filterCarnivorousCheck.isSelected();
            List<PlantDTO> filtered = viewModel.filterPlants(selectedType, carnivorousOnly);
            view.plantTable.setItems(FXCollections.observableArrayList(filtered));
            view.messageLabel.setText(MessageFormat.format(bundle.getString("filteredPlants"), filtered.size()));
        });

        view.specimenSearchButton.setOnAction(e -> {
            String query = view.specimenSearchField.getText();
            List<SpecimenDTO> results = viewModel.filterSpecimens(query);
            view.specimenTable.setItems(FXCollections.observableArrayList(results));
            view.messageLabel.setText(MessageFormat.format(bundle.getString("foundSpecimens"), results.size()));
        });

        view.exportButton.setOnAction(e -> {
            viewModel.exportToCSV();
            view.messageLabel.setText(bundle.getString("exportCSV"));
        });

        view.exportDocButton.setOnAction(e -> {
            viewModel.exportToDOC();
            view.messageLabel.setText(bundle.getString("exportDOC"));
        });
    }

    private void loadData() {
        viewModel.loadAllData();
        view.plantTable.setItems(viewModel.getPlants());
        view.specimenTable.setItems(viewModel.getSpecimens());
        view.messageLabel.setText(bundle.getString("messageDataLoaded"));
    }

    private void setupLanguageChangeListener() {
        view.languageToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selected = (RadioButton) newToggle;

                // ✅ Ensure getUserData() is not null and is a String
                Object langCode = selected.getUserData();
                if (langCode == null) return;

                switch (langCode.toString()) {
                    case "en" -> currentLocale = Locale.ENGLISH;
                    case "fr" -> currentLocale = Locale.FRENCH;
                    case "ro" -> currentLocale = new Locale("ro");
                }

                bundle = ResourceBundle.getBundle("lang.messages", currentLocale);
                updateLanguage();
            }
        });
    }

    private void updateLanguage() {
        // Buttons
        view.filterButton.setText(bundle.getString("filter"));
        view.exportButton.setText(bundle.getString("export"));
        view.exportDocButton.setText(bundle.getString("exportDoc"));
        view.specimenSearchButton.setText(bundle.getString("search"));

        // Checkboxes
        view.filterCarnivorousCheck.setText(bundle.getString("filterCarnivorous"));

        // Table column headers
        view.idColumn.setText(bundle.getString("plantId"));
        view.typeColumn.setText(bundle.getString("type"));
        view.nameColumn.setText(bundle.getString("name"));
        view.speciesColumn.setText(bundle.getString("species"));
        view.carnivoreColumn.setText(bundle.getString("carnivore"));
        view.specimenIdColumn.setText(bundle.getString("specimenId"));
        view.plantIdColumn.setText(bundle.getString("plantId"));
        view.locationColumn.setText(bundle.getString("location"));
        view.imageColumn.setText(bundle.getString("image"));

        // Initial message
        view.messageLabel.setText(bundle.getString("messageDataLoaded"));
    }
}
