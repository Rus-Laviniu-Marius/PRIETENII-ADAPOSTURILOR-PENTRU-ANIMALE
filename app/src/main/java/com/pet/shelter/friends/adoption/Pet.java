package com.pet.shelter.friends.adoption;

public class Pet {

    private int backgroundColor, imageId, age;
    private String name, location, breed, gender, size, description;
    private double weight;
    // TODO: WHEN VETERINARY IS AN ACTOR OF THE APP THEN PET VETERINARIAN DATA
    private boolean id, dewormed, sterilized, vaccines;

    public Pet(int backgroundColor, int imageId, String name, int age, double weight, String location, String size, String breed, String gender, String description) {
        this.backgroundColor = backgroundColor;
        this.imageId = imageId;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.location = location;
        this.size = size;
        this.breed = breed;
        this.gender = gender;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
