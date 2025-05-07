package org.example.controller.dto;
import org.example.model.Specimen;

public class SpecimenMapper {

    // Convert a Specimen entity to a SpecimenDTO
    public static SpecimenDTO toDTO(Specimen specimen) {
        return new SpecimenDTO(
                specimen.getSpecimen_id(),
                specimen.getPlant_id(),
                specimen.getLocation(),
                specimen.getImageUrl()
        );
    }

    // Convert a SpecimenDTO to a Specimen entity
    public static Specimen toEntity(SpecimenDTO dto) {
        Specimen specimen = new Specimen();
        specimen.setSpecimen_id(dto.getSpecimen_id());
        specimen.setPlant_id(dto.getPlant_id());
        specimen.setLocation(dto.getLocation());
        specimen.setImageUrl(dto.getImageUrl());
        return specimen;
    }
}
