package org.example.dto;

import org.example.dto.PlantDTO;
import org.example.model.Plant;


public class PlantMapper {
    public static PlantDTO toDTO(Plant plant) {
        return new PlantDTO(
                plant.getPlant_id(),
                plant.getName(),
                plant.getSpecies(),
                plant.getType(),
                plant.getCarnivore()
        );
    }

    public static Plant toEntity(PlantDTO dto) {
        return new Plant(
                dto.getPlant_id(),
                dto.getName(),
                dto.getSpecies(),
                dto.getType(),
                dto.getCarnivore()
        );
    }
}
