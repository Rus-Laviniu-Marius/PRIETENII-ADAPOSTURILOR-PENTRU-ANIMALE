package com.pet.shelter.friends.adoption.model;

public class Pet {

    private String backgroundColor;
    private String imageDownloadLink;
    private String name;
    private String location;
    private String size;
    private String breed;
    private String sex;
    private String description;
    private int age;
    private double weight;

    public Pet(String backgroundColor, String imageDownloadLink, String name, int age, double weight, String location, String size, String breed, String sex, String description) {
        this.backgroundColor = backgroundColor;
        this.imageDownloadLink = imageDownloadLink;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.location = location;
        this.size = size;
        this.breed = breed;
        this.sex = sex;
        this.description = description;
    }
//
    public Pet() {

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getImageDownloadLink() {
        return imageDownloadLink;
    }

    public void setImageIdString(String imageDownloadLink) {
        this.imageDownloadLink = imageDownloadLink;
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
