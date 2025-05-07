package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.example.controller.SpecimenController;
import org.example.dto.SpecimenDTO;
import org.example.model.SpecimenModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SpecimenView implements PropertyChangeListener {
    private final SpecimenModel model;
    private final SpecimenController controller;

    private TableView<SpecimenDTO> specimenTable = new TableView<>();
    private Label locationLabel;
    private Label imageUrlLabel;
    private TextField locationField = new TextField();
    private TextField imageUrlField = new TextField();
    private Label messageLabel = new Label();

    private VBox root;
    private final ToggleGroup languageToggleGroup = new ToggleGroup();
    private Locale currentLocale;
    private ResourceBundle bundle;
    private HBox buttons;

    public SpecimenView(SpecimenModel model, SpecimenController controller, Locale locale) {
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
        specimenTable.getColumns().clear(); // Clear previous columns
        specimenTable.getColumns().addAll(
                createColumn(bundle.getString("column.id"), "specimen_id", 50),
                createColumn(bundle.getString("column.location"), "location", 150),
                createImageColumn(bundle.getString("column.imageUrl"), "imageUrl", 150),
                createColumn(bundle.getString("column.plantId"), "plant_id", 100)  // Add plant_id column
        );

        specimenTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                locationField.setText(newSel.getLocation());
                imageUrlField.setText(newSel.getImageUrl());
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

        locationLabel = new Label(bundle.getString("label.location"));
        imageUrlLabel = new Label(bundle.getString("label.imageUrl"));

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setVgap(5); form.setHgap(5);
        form.addRow(0, locationLabel, locationField);
        form.addRow(1, imageUrlLabel, imageUrlField);

        buttons = new HBox(10, add, update, delete);
        buttons.setPadding(new Insets(10));

        // Language selector setup
        HBox languageSelector = createLanguageSelector();

        root = new VBox(10, languageSelector, specimenTable, form, buttons, messageLabel);
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
        updateLanguageForUI();
        controller.load();  // Refresh table content
    }

    private void updateLanguageForUI() {
        // Update UI components with language changes
        for (int i = 0; i < specimenTable.getColumns().size(); i++) {
            TableColumn<SpecimenDTO, ?> col = specimenTable.getColumns().get(i);
            switch (i) {
                case 0 -> col.setText(bundle.getString("column.specimenid"));
                case 1 -> col.setText(bundle.getString("column.location"));
                case 2 -> col.setText(bundle.getString("column.imageUrl"));
                case 3 -> col.setText(bundle.getString("column.plantId")); // Update plant_id column header
            }
        }

        messageLabel.setText(bundle.getString("message.specimenRefreshed"));

        // Update button labels
        ((Button) buttons.getChildren().get(0)).setText(bundle.getString("button.addSpecimen"));
        ((Button) buttons.getChildren().get(1)).setText(bundle.getString("button.updateSpecimen"));
        ((Button) buttons.getChildren().get(2)).setText(bundle.getString("button.deleteSpecimen"));
        locationLabel.setText(bundle.getString("label.location"));
        imageUrlLabel.setText(bundle.getString("label.imageUrl"));
    }

    private TableColumn<SpecimenDTO, ?> createColumn(String title, String prop, int width) {
        TableColumn<SpecimenDTO, Object> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setPrefWidth(width);
        return col;
    }

    private TableColumn<SpecimenDTO, ?> createImageColumn(String title, String prop, int width) {
        TableColumn<SpecimenDTO, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setCellFactory(cell -> new TableCell<SpecimenDTO, String>() {
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        // Load image from local file path
                        File file = new File(imagePath);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString()); // Convert file path to URI
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(100); // Set image width
                            imageView.setFitHeight(100); // Set image height
                            setGraphic(imageView);
                        } else {
                            setGraphic(null); // If the file doesn't exist, show nothing
                        }
                    } catch (Exception e) {
                        setGraphic(null); // If an error occurs (e.g., invalid file), show nothing
                    }
                }
            }
        });
        col.setPrefWidth(width);
        return col;
    }


    private SpecimenDTO getDTOFromFields() {
        SpecimenDTO selected = specimenTable.getSelectionModel().getSelectedItem();
        int plantId = selected != null ? selected.getPlant_id() : 0; // Get plant_id if item is selected
        return new SpecimenDTO(0, plantId, locationField.getText(), imageUrlField.getText());
    }

    private SpecimenDTO getDTOFromFieldsWithId() {
        SpecimenDTO selected = specimenTable.getSelectionModel().getSelectedItem();
        if (selected == null) return null;
        return new SpecimenDTO(
                selected.getSpecimen_id(),
                selected.getPlant_id(),
                locationField.getText(),
                imageUrlField.getText());
    }

    private void clearFields() {
        locationField.clear(); imageUrlField.clear();
    }

    public VBox getView() {
        return root;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("specimens".equals(evt.getPropertyName())) {
            List<SpecimenDTO> updatedSpecimens = (List<SpecimenDTO>) evt.getNewValue();
            specimenTable.getItems().setAll(updatedSpecimens);
            messageLabel.setText(bundle.getString("message.refreshed"));
        }
    }
}