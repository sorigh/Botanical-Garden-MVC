package org.example.dto;

public class SpecimenDTO {
    private int specimen_id;
    private int plant_id;
    private String location;
    private String imageUrl;

    // Constructor for creating a new SpecimenDTO
    public SpecimenDTO(int specimen_id, int plant_id, String location, String imageUrl) {
        this.specimen_id = specimen_id;
        this.plant_id = plant_id;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    // Getters and setters for all fields
    public int getSpecimen_id() {
        return specimen_id;
    }

    public void setSpecimen_id(int specimen_id) {
        this.specimen_id = specimen_id;
    }

    public int getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(int plant_id) {
        this.plant_id = plant_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "SpecimenDTO{" +
                "specimen_id=" + specimen_id +
                ", plant_id=" + plant_id +
                ", location='" + location + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
