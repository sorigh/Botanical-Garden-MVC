package org.example.controller;

import org.example.dto.SpecimenDTO;
import org.example.model.SpecimenModel;

public class SpecimenController {
    private final SpecimenModel model;

    public SpecimenController(SpecimenModel model) {
        this.model = model;
    }

    public void load() {
        model.loadSpecimens();
    }

    public void add(SpecimenDTO dto) {
        model.addSpecimen(dto);
    }

    public void update(SpecimenDTO dto) {
        model.updateSpecimen(dto);
    }

    public void delete(SpecimenDTO dto) {
        model.deleteSpecimen(dto);
    }
}
