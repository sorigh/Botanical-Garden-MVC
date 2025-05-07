package org.example.model.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.controller.dto.PlantDTO;
import org.example.controller.dto.PlantMapper;
import org.example.controller.dto.SpecimenDTO;
import org.example.controller.dto.SpecimenMapper;
import org.example.model.Plant;
import org.example.model.PlantExporter;
import org.example.model.Specimen;
import org.example.model.repository.PlantRepository;
import org.example.model.repository.SpecimenRepository;

import java.util.List;
import java.util.stream.Collectors;


public class GardenViewModel {
    private final PlantRepository plantRepository;
    private final SpecimenRepository specimenRepository;

    public final ObservableList<PlantDTO> plants = FXCollections.observableArrayList();
    public final ObservableList<SpecimenDTO> specimens = FXCollections.observableArrayList();

    public GardenViewModel(PlantRepository plantRepository, SpecimenRepository specimenRepository) {
        this.plantRepository = plantRepository;
        this.specimenRepository = specimenRepository;
        loadAllData();
    }

    public void loadAllData() {
        plants.setAll(
                plantRepository.getTableContent().stream()
                        .map(PlantMapper::toDTO)
                        .collect(Collectors.toList())
        );

        specimens.setAll(
                specimenRepository.getTableContent().stream()
                        .map(SpecimenMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    public List<PlantDTO> filterPlants(String type, boolean onlyCarnivorous) {
        List<Plant> allPlants = plantRepository.getTableContent();

        List<PlantDTO> filtered = allPlants.stream()
                .filter(p -> type.equals("No Filter") || p.getType().equalsIgnoreCase(type))
                .filter(p -> !onlyCarnivorous || p.getCarnivore() == 1)
                .map(PlantMapper::toDTO)
                .collect(Collectors.toList());

        plants.setAll(filtered);
        return filtered;
    }

    public void searchSpecimens(String query) {
        List<Specimen> allSpecimens = specimenRepository.getTableContent();

        List<SpecimenDTO> filtered = allSpecimens.stream()
                .map(SpecimenMapper::toDTO)
                .filter(s -> query == null || query.isBlank() || (
                        String.valueOf(s.getSpecimen_id()).contains(query) ||
                                String.valueOf(s.getPlant_id()).contains(query) ||
                                s.getLocation().toLowerCase().contains(query.toLowerCase())
                ))
                .collect(Collectors.toList());

        specimens.setAll(filtered);
    }

    public List<String> getAvailablePlantTypes() {
        return plantRepository.getTableContent().stream()
                .map(Plant::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    public void exportPlantsToCSV(String filePath) {
        List<Plant> allPlants = plantRepository.getTableContent();
        PlantExporter.exportToCSV(allPlants, filePath);
    }

    public void exportPlantsToDOC(String filePath) {
        List<Plant> allPlants = plantRepository.getTableContent();
        PlantExporter.exportToDOC(allPlants, filePath);
    }

    public void exportToCSV() {
        PlantExporter.exportToCSV(plantRepository.getTableContent(),"plants.csv");
    }

    public void exportToDOC() {
        PlantExporter.exportToDOC(plantRepository.getTableContent(),"plants.docx");
    }

    public List<String> getAvailableTypes() {
        return plants.stream()
                .map(PlantDTO::getType)
                .distinct()
                .toList(); // or .collect(Collectors.toList()) if you're using Java 8
    }

    public List<SpecimenDTO> filterSpecimens(String query) {
        List<Specimen> allSpecimens = specimenRepository.getTableContent();

        List<SpecimenDTO> filtered = allSpecimens.stream()
                .map(SpecimenMapper::toDTO)
                .filter(s -> query == null || query.isBlank() || (
                        String.valueOf(s.getSpecimen_id()).contains(query) ||
                                String.valueOf(s.getPlant_id()).contains(query) ||
                                s.getLocation().toLowerCase().contains(query.toLowerCase())
                ))
                .collect(Collectors.toList());

        specimens.setAll(filtered);
        return filtered;
    }


    public ObservableList<PlantDTO> getPlants() {
        return plants;
    }

    public ObservableList<SpecimenDTO> getSpecimens() {
        return specimens;
    }

}