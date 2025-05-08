package org.example.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.controller.dto.PlantDTO;
import org.example.controller.dto.SpecimenDTO;
import org.example.model.Observable;
import org.example.model.Observer;
import org.example.model.viewmodel.GardenViewModel;
import org.example.model.viewmodel.PlantViewModel;

import java.util.List;

public class GardenView implements Observer {
    // Plant table and columns
    public final TableView<PlantDTO> plantTable = new TableView<>();
    public final TableColumn<PlantDTO, Integer> idColumn = new TableColumn<>("Id");
    public final TableColumn<PlantDTO, String> typeColumn = new TableColumn<>("Type");
    public final TableColumn<PlantDTO, String> nameColumn = new TableColumn<>("Name");
    public final TableColumn<PlantDTO, String> speciesColumn = new TableColumn<>("Species");
    public final TableColumn<PlantDTO, Integer> carnivoreColumn = new TableColumn<>("Carnivorous");

    // Specimen table and columns
    public final TableView<SpecimenDTO> specimenTable = new TableView<>();
    public final TableColumn<SpecimenDTO, Integer> specimenIdColumn = new TableColumn<>("Specimen Id");
    public final TableColumn<SpecimenDTO, Integer> plantIdColumn = new TableColumn<>("Plant Id");
    public final TableColumn<SpecimenDTO, String> locationColumn = new TableColumn<>("Location");
    public final TableColumn<SpecimenDTO, String> imageColumn = new TableColumn<>("Image");

    // Search controls
    public final TextField specimenSearchField = new TextField();
    public final Button specimenSearchButton = new Button("Search Specimens");

    // Filter + export controls
    public final ComboBox<String> filterTypeBox = new ComboBox<>();
    public final CheckBox filterCarnivorousCheck = new CheckBox("Carnivorous");
    public final Button filterButton = new Button("Filter Plants");
    public final Button exportButton = new Button("Export Plants to CSV");
    public final Button exportDocButton = new Button("Export Plants to DOC");

    // Language controls
    public final ToggleGroup languageToggleGroup = new ToggleGroup();
    public final RadioButton englishButton = new RadioButton("English");
    public final RadioButton frenchButton = new RadioButton("Francais");
    public final RadioButton romanianButton = new RadioButton("Romana");

    // Message
    public final Label messageLabel = new Label();

    // Root
    public final GridPane root = new GridPane();

    public GardenView() {
        setupLayout();
    }

    private void setupLayout() {
        // ✅ Assign userData to radio buttons
        englishButton.setUserData("en");
        frenchButton.setUserData("fr");
        romanianButton.setUserData("ro");

        // Group them
        englishButton.setToggleGroup(languageToggleGroup);
        frenchButton.setToggleGroup(languageToggleGroup);
        romanianButton.setToggleGroup(languageToggleGroup);
        englishButton.setSelected(true); // default

        // Language selector
        HBox languageSelector = new HBox(10, new Label("Language:"), englishButton, frenchButton, romanianButton);
        languageSelector.setPadding(new Insets(5, 0, 5, 0));

        // Plant table
        plantTable.getColumns().addAll(idColumn, typeColumn, nameColumn, speciesColumn, carnivoreColumn);
        plantTable.setPrefWidth(500);

        // Specimen table
        specimenTable.getColumns().addAll(specimenIdColumn, plantIdColumn, locationColumn, imageColumn);
        specimenTable.setPrefWidth(500);

        // Search controls
        specimenSearchField.setPromptText("Search specimens...");
        HBox searchBox = new HBox(10, specimenSearchField, specimenSearchButton);

        // Left side VBox (tables + search)
        VBox leftBox = new VBox(20, plantTable, specimenTable, searchBox);
        leftBox.setSpacing(20);

        // Right side VBox (filters and export)
        VBox rightBox = new VBox(10, filterTypeBox, filterCarnivorousCheck, filterButton, exportButton, exportDocButton);
        rightBox.setPrefWidth(200);

        // Combined in HBox
        HBox mainBox = new HBox(10, leftBox, rightBox);
        GridPane.setColumnSpan(mainBox, 2);

        // Final layout
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));

        // Add components to root
        root.add(languageSelector, 0, 0);
        GridPane.setColumnSpan(languageSelector, 2);

        root.add(mainBox, 0, 1);
        root.add(messageLabel, 0, 2);
        GridPane.setColumnSpan(messageLabel, 2);
    }

    public GridPane getView() {
        return root;
    }

    @Override
    public void update(Observable observable) {
        if (observable instanceof GardenViewModel viewModel) {
            messageLabel.setText("Export Generated!");

            // Print output message when observer is triggered
            System.out.println("Observer triggered: Export Generated.");
        }
    }
}
