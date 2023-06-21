package com.pet.shelter.friends.pets.sheltered;

public class ShelteredPetData {
    private String shelterAdministratorId;
    private String petImage1DownloadLink;
    private String petImage2DownloadLink;
    private String petImage3DownloadLink;
    private String petName;
    private String petType;
    private String petBreed;
    private String petAge;
    private String petSize;
    private String petSex;
    private String petLocation;
    private String petWeight;
    private String petColor;
    private String petDescription;
    private String spayedOrNeutered;
    private String dewormed;
    private String vaccines;
    private String fitForChildren;
    private String fitForGuarding;
    private String friendlyWithPets;
    private String favorite;

    public ShelteredPetData() { }

    public ShelteredPetData(String shelterAdministratorId, String petImage1DownloadLink, String petName, String petType, String petBreed, String petAge, String petSize, String petSex, String petLocation, String petWeight, String petColor, String petDescription, String spayedOrNeutered, String dewormed, String vaccines, String fitForChildren, String fitForGuarding, String friendlyWithPets, String favorite) {
        this.shelterAdministratorId = shelterAdministratorId;
        this.petImage1DownloadLink = petImage1DownloadLink;
        this.petName = petName;
        this.petType = petType;
        this.petBreed = petBreed;
        this.petAge = petAge;
        this.petSize = petSize;
        this.petSex = petSex;
        this.petLocation = petLocation;
        this.petWeight = petWeight;
        this.petColor = petColor;
        this.petDescription = petDescription;
        this.spayedOrNeutered = spayedOrNeutered;
        this.dewormed = dewormed;
        this.vaccines = vaccines;
        this.fitForChildren = fitForChildren;
        this.fitForGuarding = fitForGuarding;
        this.friendlyWithPets = friendlyWithPets;
        this.favorite = favorite;
    }

    public String getShelterAdministratorId() {
        return shelterAdministratorId;
    }

    public void setShelterAdministratorId(String shelterAdministratorId) {
        this.shelterAdministratorId = shelterAdministratorId;
    }

    public String getPetImage1DownloadLink() {
        return petImage1DownloadLink;
    }

    public void setPetImage1DownloadLink(String petImage1DownloadLink) {
        this.petImage1DownloadLink = petImage1DownloadLink;
    }

    public String getPetImage2DownloadLink() {
        return petImage2DownloadLink;
    }

    public void setPetImage2DownloadLink(String petImage2DownloadLink) {
        this.petImage2DownloadLink = petImage2DownloadLink;
    }

    public String getPetImage3DownloadLink() {
        return petImage3DownloadLink;
    }

    public void setPetImage3DownloadLink(String petImage3DownloadLink) {
        this.petImage3DownloadLink = petImage3DownloadLink;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getPetSize() {
        return petSize;
    }

    public void setPetSize(String petSize) {
        this.petSize = petSize;
    }

    public String getPetSex() {
        return petSex;
    }

    public void setPetSex(String petSex) {
        this.petSex = petSex;
    }

    public String getPetLocation() {
        return petLocation;
    }

    public void setPetLocation(String petLocation) {
        this.petLocation = petLocation;
    }

    public String getPetWeight() {
        return petWeight;
    }

    public void setPetWeight(String petWeight) {
        this.petWeight = petWeight;
    }

    public String getPetColor() {
        return petColor;
    }

    public void setPetColor(String petColor) {
        this.petColor = petColor;
    }

    public String getPetDescription() {
        return petDescription;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public String getSpayedOrNeutered() {
        return spayedOrNeutered;
    }

    public void setSpayedOrNeutered(String spayedOrNeutered) {
        this.spayedOrNeutered = spayedOrNeutered;
    }

    public String getDewormed() {
        return dewormed;
    }

    public void setDewormed(String dewormed) {
        this.dewormed = dewormed;
    }

    public String getVaccines() {
        return vaccines;
    }

    public void setVaccines(String vaccines) {
        this.vaccines = vaccines;
    }

    public String getFitForChildren() {
        return fitForChildren;
    }

    public void setFitForChildren(String fitForChildren) {
        this.fitForChildren = fitForChildren;
    }

    public String getFitForGuarding() {
        return fitForGuarding;
    }

    public void setFitForGuarding(String fitForGuarding) {
        this.fitForGuarding = fitForGuarding;
    }

    public String getFriendlyWithPets() {
        return friendlyWithPets;
    }

    public void setFriendlyWithPets(String friendlyWithPets) {
        this.friendlyWithPets = friendlyWithPets;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
