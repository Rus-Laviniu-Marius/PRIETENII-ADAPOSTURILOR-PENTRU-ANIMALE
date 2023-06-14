package com.pet.shelter.friends.profile.services;

public class ActiveServiceData {
    private String providerUserProfileImage;
    private String name;
    private String email;
    private String phoneNumber;
    private String webpageLink;
    private String cityStateCountry;
    private String address;
    private String description;

    public ActiveServiceData() {}

    public ActiveServiceData(String providerUserProfileImage,
                             String name,
                             String email,
                             String phoneNumber,
                             String webpageLink,
                             String cityStateCountry,
                             String address,
                             String description) {
        this.providerUserProfileImage = providerUserProfileImage;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.webpageLink = webpageLink;
        this.cityStateCountry = cityStateCountry;
        this.address = address;
        this.description = description;
    }

    public String getProviderUserProfileImage() {
        return providerUserProfileImage;
    }

    public void setProviderUserProfileImage(String providerUserProfileImage) {
        this.providerUserProfileImage = providerUserProfileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebpageLink() {
        return webpageLink;
    }

    public void setWebpageLink(String webpageLink) {
        this.webpageLink = webpageLink;
    }

    public String getCityStateCountry() {
        return cityStateCountry;
    }

    public void setCityStateCountry(String cityStateCountry) {
        this.cityStateCountry = cityStateCountry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
