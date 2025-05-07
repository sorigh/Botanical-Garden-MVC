package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.controller.PlantController;
import org.example.dto.PlantDTO;
import org.example.model.PlantModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PlantView implements PropertyChangeListener {
    private final PlantModel model;
    private final PlantController controller;

    private TableView<PlantDTO> plantTable = new TableView<>();
    private Label nameLabel;
    private Label typeLabel;
    private Label speciesLabel;
    private Label carnivoreLabel;
    private TextField nameField = new TextField();
    private TextField typeField = new TextField();
    private TextField speciesField = new TextField();
    private TextField carnivoreField = new TextField();
    private Label messageLabel = new Label();

    private VBox root;
    private final ToggleGroup languageToggleGroup = new ToggleGroup();
    private Locale currentLocale;
    private ResourceBundle bundle;
    private HBox buttons;

    public PlantView(PlantModel model, PlantController controller, Locale locale) {
        this.model = model;
        this.controller = controller;
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("lang.messages", currentLocale);
        model.addPropertyChangeListener(this);
        initUI();
        controller.load();
    }

    private void initUI() {
        // Setup table with localized headers
        plantTable.getColumns().clear(); // Clear previous columns
        plantTable.getColumns().addAll(
                createColumn(bundle.getString("column.id"), "plant_id", 50),
                createColumn(bundle.getString("column.name"), "name", 150),
                createColumn(bundle.getString("column.type"), "type", 150),
                createColumn(bundle.getString("column.species"), "species", 150),
                createColumn(bundle.getString("column.carnivore"), "carnivore", 80)
        );

        plantTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nameField.setText(newSel.getName());
                typeField.setText(newSel.getType());
                speciesField.setText(newSel.getSpecies());
                carnivoreField.setText(String.valueOf(newSel.getCarnivore()));
            }
        });

        Button add = new Button(bundle.getString("button.add"));
        add.setOnAction(e -> {
            controller.add(getDTOFromFields());
            clearFields();
        });

        Button update = new Button(bundle.getString("button.update"));
        update.setOnAction(e -> {
            controller.update(getDTOFromFieldsWithId());
            clearFields();
        });

        Button delete = new Button(bundle.getString("button.delete"));
        delete.setOnAction(e -> {
            controller.delete(getDTOFromFieldsWithId());
            clearFields();
        });

        nameLabel = new Label(bundle.getString("label.name"));
        typeLabel = new Label(bundle.getString("label.type"));
        speciesLabel = new Label(bundle.getString("label.species"));
        carnivoreLabel = new Label(bundle.getString("label.carnivore"));

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setVgap(5); form.setHgap(5);
        form.addRow(0, nameLabel, nameField);
        form.addRow(1, typeLabel, typeField);
        form.addRow(2, speciesLabel, speciesField);
        form.addRow(3, carnivoreLabel, carnivoreField);


        buttons = new HBox(10, add, update, delete);
        buttons.setPadding(new Insets(10));

        // Language selector setup
        HBox languageSelector = createLanguageSelector();

        root = new VBox(10, languageSelector, plantTable, form, buttons, messageLabel);
        root.setPadding(new Insets(10));
    }

    private HBox createLanguageSelector() {
        RadioButton english = new RadioButton("English");
        RadioButton french = new RadioButton("Francais");
        RadioButton romanian = new RadioButton("Romana");

        english.setToggleGroup(languageToggleGroup);
        french.setToggleGroup(languageToggleGroup);
        romanian.setToggleGroup(languageToggleGroup);

        // Set the initial language based on currentLocale
        switch (currentLocale.getLanguage()) {
            case "fr" -> french.setSelected(true);
            case "ro" -> romanian.setSelected(true);
            default -> english.setSelected(true);
        }

        english.setOnAction(e -> switchLanguage(new Locale("en")));
        french.setOnAction(e -> switchLanguage(new Locale("fr")));
        romanian.setOnAction(e -> switchLanguage(new Locale("ro")));

        return new HBox(10, new Label(bundle.getString("label.language")), english, french, romanian);
    }

    private void switchLanguage(Locale locale) {
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("lang.messages", currentLocale);

        // Update UI components based on the new language
        updateLanguageForUI();

        controller.load();  // Refresh table content
    }

    private void updateLanguageForUI() {
        // Update all UI components that require language changes
        for (int i = 0; i < plantTable.getColumns().size(); i++) {
            TableColumn<PlantDTO, ?> col = plantTable.getColumns().get(i);
            switch (i) {
                case 0 -> col.setText(bundle.getString("column.id"));
                case 1 -> col.setText(bundle.getString("column.name"));
                case 2 -> col.setText(bundle.getString("column.type"));
                case 3 -> col.setText(bundle.getString("column.species"));
                case 4 -> col.setText(bundle.getString("column.carnivore"));
            }
        }

        messageLabel.setText(bundle.getString("message.refreshed"));

        // Update button labels
        ((Button) buttons.getChildren().get(0)).setText(bundle.getString("button.add"));
        ((Button) buttons.getChildren().get(1)).setText(bundle.getString("button.update"));
        ((Button) buttons.getChildren().get(2)).setText(bundle.getString("button.delete"));


        nameLabel.setText(bundle.getString("label.name"));
        typeLabel.setText(bundle.getString("label.type"));
        speciesLabel.setText(bundle.getString("label.species"));
        carnivoreLabel.setText(bundle.getString("label.carnivore"));


    }

    private TableColumn<PlantDTO, ?> createColumn(String title, String prop, int width) {
        TableColumn<PlantDTO, Object> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setPrefWidth(width);
        return col;
    }

    private PlantDTO getDTOFromFields() {
        return new PlantDTO(0,
                nameField.getText(),
                speciesField.getText(),
                typeField.getText(),
                Integer.parseInt(carnivoreField.getText()));
    }

    private PlantDTO getDTOFromFieldsWithId() {
        PlantDTO selected = plantTable.getSelectionModel().getSelectedItem();
        if (selected == null) return null;
        return new PlantDTO(
                selected.getPlant_id(),
                nameField.getText(),
                speciesField.getText(),
                typeField.getText(),
                Integer.parseInt(carnivoreField.getText()));
    }

    private void clearFields() {
        nameField.clear(); typeField.clear(); speciesField.clear(); carnivoreField.clear();
    }

    public VBox getView() {
        return root;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("plants".equals(evt.getPropertyName())) {
            List<PlantDTO> updatedPlants = (List<PlantDTO>) evt.getNewValue();
            plantTable.getItems().setAll(updatedPlants);
            messageLabel.setText(bundle.getString("message.refreshed"));
        }
    }

}
