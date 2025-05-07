package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.controller.dto.SpecimenDTO;

public class SpecimenView {
    public final TableView<SpecimenDTO> specimenTable = new TableView<>();
    public final TableColumn<SpecimenDTO, Integer> idColumn = new TableColumn<>();
    public final TableColumn<SpecimenDTO, String> locationColumn = new TableColumn<>();
    public final TableColumn<SpecimenDTO, String> imageUrlColumn = new TableColumn<>();
    public final TableColumn<SpecimenDTO, Integer> plantIdColumn = new TableColumn<>();

    public final TextField locationField = new TextField();
    public final TextField imageUrlField = new TextField();

    public final Button addButton = new Button();
    public final Button updateButton = new Button();
    public final Button deleteButton = new Button();
    public final Button clearButton = new Button();

    public final Label messageLabel = new Label();
    public final Label locationLabel = new Label();
    public final Label imageUrlLabel = new Label();

    public final ToggleGroup languageToggleGroup = new ToggleGroup();
    public final RadioButton englishButton = new RadioButton("English");
    public final RadioButton frenchButton = new RadioButton("Francais");
    public final RadioButton romanianButton = new RadioButton("Romana");

    public final VBox root = new VBox();

    public SpecimenView() {
        setupLayout();
    }

    private void setupLayout() {
        // Table
        specimenTable.getColumns().addAll(idColumn, locationColumn, imageUrlColumn, plantIdColumn);

        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, locationLabel, locationField);
        form.addRow(1, imageUrlLabel, imageUrlField);

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
        root.getChildren().addAll(languageSelector, specimenTable, form, buttons, messageLabel);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
    }

    public VBox getView() {
        return root;
    }
}
