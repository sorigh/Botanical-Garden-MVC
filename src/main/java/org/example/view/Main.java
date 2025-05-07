package org.example.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.controller.PlantController;
import org.example.controller.SpecimenController;
import org.example.model.PlantModel;
import org.example.model.SpecimenModel;
import org.example.model.repository.PlantRepository;
import org.example.model.repository.SpecimenRepository;

import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Default language
        Locale locale = Locale.ENGLISH;

        // Init PlantView
        PlantRepository plantRepository = new PlantRepository();
        PlantModel plantModel = new PlantModel(plantRepository);
        PlantView plantView = new PlantView();
        PlantController plantController = new PlantController(plantModel, plantView, locale);


        // Init SpecimenView
        SpecimenRepository specimenRepository = new SpecimenRepository();
        SpecimenModel specimenModel = new SpecimenModel(specimenRepository);
        //SpecimenView specimenView = new SpecimenView();
        //SpecimenController specimenController = new SpecimenController(specimenModel,specimenView,locale);
        // Dummy placeholders for Specimen and Garden views (replace with actual ones later)
        HBox gardenView = new HBox(new Button("Garden View - TBD"));

        // Navigation buttons
        Button buttonPlants = new Button("Plants");
        Button buttonSpecimens = new Button("Specimens");
        Button buttonGarden = new Button("Garden");

        HBox menuBar = new HBox(20, buttonPlants, buttonSpecimens, buttonGarden);
        menuBar.setPadding(new Insets(10));

        // Root layout
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(plantView.getView()); // Default view

        // Button actions
        buttonPlants.setOnAction(e -> root.setCenter(plantView.getView()));
        //buttonSpecimens.setOnAction(e -> root.setCenter(specimenView.getView()));
        buttonGarden.setOnAction(e -> root.setCenter(gardenView));

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Botanical Garden");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}