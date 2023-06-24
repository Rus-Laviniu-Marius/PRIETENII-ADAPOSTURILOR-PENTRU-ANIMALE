package com.pet.shelter.friends.pets;

public class ShelterData {
    private String address;
    private String email;
    private String iban;
    private String latitude;
    private String longitude;
    private String name;
    private String ourAdoptionPolicy;
    private String ourMission;
    private String phoneNumber;
    private String profileImageDownloadLink;
    private String webPageLink;

    public ShelterData() { }

    public ShelterData(String address, String email, String iban, String latitude, String longitude, String name, String ourAdoptionPolicy, String ourMission, String phoneNumber, String profileImageDownloadLink, String webPageLink) {
        this.address = address;
        this.email = email;
        this.iban = iban;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.ourAdoptionPolicy = ourAdoptionPolicy;
        this.ourMission = ourMission;
        this.phoneNumber = phoneNumber;
        this.profileImageDownloadLink = profileImageDownloadLink;
        this.webPageLink = webPageLink;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getIban() {
        return iban;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getOurAdoptionPolicy() {
        return ourAdoptionPolicy;
    }

    public String getOurMission() {
        return ourMission;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfileImageDownloadLink() {
        return profileImageDownloadLink;
    }

    public String getWebPageLink() {
        return webPageLink;
    }
}
