package com.pet.shelter.friends.pets.shelter;

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

    public ShelterData(String address,
                       String email,
                       String iban,
                       String latitude,
                       String longitude,
                       String name,
                       String ourAdoptionPolicy,
                       String ourMission,
                       String phoneNumber,
                       String profileImageDownloadLink,
                       String webPageLink) {
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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOurAdoptionPolicy() {
        return ourAdoptionPolicy;
    }

    public void setOurAdoptionPolicy(String ourAdoptionPolicy) {
        this.ourAdoptionPolicy = ourAdoptionPolicy;
    }

    public String getOurMission() {
        return ourMission;
    }

    public void setOurMission(String ourMission) {
        this.ourMission = ourMission;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageDownloadLink() {
        return profileImageDownloadLink;
    }

    public void setProfileImageDownloadLink(String profileImageDownloadLink) {
        this.profileImageDownloadLink = profileImageDownloadLink;
    }

    public String getWebPageLink() {
        return webPageLink;
    }

    public void setWebPageLink(String webPageLink) {
        this.webPageLink = webPageLink;
    }
}
