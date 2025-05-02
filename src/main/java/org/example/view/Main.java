package org.example.view;


import javafx.application.Application;
import org.example.controller.PlantListController;
import javafx.stage.Stage;
import org.example.model.repository.PlantRepository;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        /*
        PlantView plantView = new PlantViewImpl();
        SpecimenView specimenView = new SpecimenViewImpl();
        GardenView gardenView = new GardenViewImpl();

        Button buttonPlants = new Button("Plants");
        Button buttonSpecimens = new Button("Specimens");
        Button buttonGarden = new Button("Garden");


        HBox menuBar = new HBox(20, buttonPlants,buttonSpecimens,buttonGarden);
        menuBar.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(plantView.getView());

        buttonPlants.setOnAction(e -> root.setCenter(plantView.getView()));
        buttonSpecimens.setOnAction(e->root.setCenter(specimenView.getView()));
        buttonGarden.setOnAction(e->root.setCenter(gardenView.getView()));
        Scene scene = new Scene(root, 1000, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Botanical Garden");
        primaryStage.show();
         */
        PlantRepository plantRepository = new PlantRepository();
        PlantListView plantListView = new PlantListView(primaryStage);
        PlantListController plantListController = new PlantListController(plantListView, plantRepository);
        plantListController.updatePlantListView();

    }


    public static void main(String[] args) {
        launch(args);
    }


}