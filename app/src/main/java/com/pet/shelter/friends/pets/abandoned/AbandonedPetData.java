package com.pet.shelter.friends.pets.abandoned;

public class AbandonedPetData {
    private String petType;
    private String petImage1DownloadLink;
    private String petDescription;
    private String placeDescription;
    private String petLocationLatitude;
    private String petLocationLongitude;

    public AbandonedPetData() {
    }

    public AbandonedPetData(String petType, String petImage1DownloadLink, String petDescription, String placeDescription, String petLocationLatitude, String petLocationLongitude) {
        this.petType = petType;
        this.petImage1DownloadLink = petImage1DownloadLink;
        this.petDescription = petDescription;
        this.placeDescription = placeDescription;
        this.petLocationLatitude = petLocationLatitude;
        this.petLocationLongitude = petLocationLongitude;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetImage1DownloadLink() {
        return petImage1DownloadLink;
    }

    public void setPetImage1DownloadLink(String petImage1DownloadLink) {
        this.petImage1DownloadLink = petImage1DownloadLink;
    }

    public String getPetDescription() {
        return petDescription;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public String getPetLocationLatitude() {
        return petLocationLatitude;
    }

    public void setPetLocationLatitude(String petLocationLatitude) {
        this.petLocationLatitude = petLocationLatitude;
    }

    public String getPetLocationLongitude() {
        return petLocationLongitude;
    }

    public void setPetLocationLongitude(String petLocationLongitude) {
        this.petLocationLongitude = petLocationLongitude;
    }
}