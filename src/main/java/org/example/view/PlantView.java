package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.controller.dto.PlantDTO;

public class PlantView {
    public final TableView<PlantDTO> plantTable = new TableView<>();
    public final TableColumn<PlantDTO, Integer> idColumn = new TableColumn<>();
    public final TableColumn<PlantDTO, String> nameColumn = new TableColumn<>();
    public final TableColumn<PlantDTO, String> typeColumn = new TableColumn<>();
    public final TableColumn<PlantDTO, String> speciesColumn = new TableColumn<>();
    public final TableColumn<PlantDTO, Integer> carnivoreColumn = new TableColumn<>();

    public final TextField nameField = new TextField();
    public final TextField typeField = new TextField();
    public final TextField speciesField = new TextField();
    public final TextField carnivoreField = new TextField();

    public final Button addButton = new Button();
    public final Button updateButton = new Button();
    public final Button deleteButton = new Button();
    public final Button clearButton = new Button();

    public final Label messageLabel = new Label();
    public final Label nameLabel = new Label();
    public final Label typeLabel = new Label();
    public final Label speciesLabel = new Label();
    public final Label carnivoreLabel = new Label();

    public final ToggleGroup languageToggleGroup = new ToggleGroup();
    public final RadioButton englishButton = new RadioButton("English");
    public final RadioButton frenchButton = new RadioButton("Francais");
    public final RadioButton romanianButton = new RadioButton("Romana");

    public final VBox root = new VBox();

    public PlantView() {
        setupLayout();
    }

    private void setupLayout() {
        // Table
        plantTable.getColumns().addAll(idColumn, nameColumn, typeColumn, speciesColumn, carnivoreColumn);

        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, nameLabel, nameField);
        form.addRow(1, typeLabel, typeField);
        form.addRow(2, speciesLabel, speciesField);
        form.addRow(3, carnivoreLabel, carnivoreField);

        // Buttons
        HBox buttons = new HBox(10, addButton, updateButton, deleteButton, clearButton);

        // Language selector
        englishButton.setToggleGroup(languageToggleGroup);
        frenchButton.setToggleGroup(languageToggleGroup);
        romanianButton.setToggleGroup(languageToggleGroup);
        englishButton.setSelected(true); // default

        HBox languageSelector = new HBox(10, new Label("Language:"), englishButton, frenchButton, romanianButton);
        languageSelector.setPadding(new Insets(5, 0, 5, 0));

        // Assemble UI
        root.getChildren().addAll(languageSelector, plantTable, form, buttons, messageLabel);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
    }

    public VBox getView() {
        return root;
    }
}
