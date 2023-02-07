package com.pet.shelter.friends.adoption.model;

import androidx.annotation.NonNull;

public class Pet {

    private String backgroundColor;
    private String imageDownloadLink;
    private String name;
    private String location;
    private String size;
    private String breed;
    private String sex;
    private String description;
    private String favorite;
    private String selected;
    private String type;
    private boolean fitForChildren, fitForGuarding;
    private int age;
    private double weight;

    public Pet(String backgroundColor, String imageDownloadLink, String name, int age, double weight,
               String location, String size, String breed, String sex, String description,
               String favorite, String selected, String type, boolean fitForChildren,
               boolean fitForGuarding) {
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
        this.favorite = favorite;
        this.selected = selected;
        this.type = type;
        this.fitForChildren = fitForChildren;
        this.fitForGuarding = fitForGuarding;
    }

    public Pet() {

    }

    @Override
    public String toString() {
        return name + " is a " + type.toLowerCase() + " of around " + age + " years old. " +
                "It is a " + size.toLowerCase() + " size, " + sex.toLowerCase() + ", " + breed + " breed " +
                type.toLowerCase() + ". " + name + " description: " + description + ". If you happen to be around the city of " +
                location + ", you can meet " + name + " at the local shelter." + "\n\n" +
                "In the mean time you can see it by clicking the following link: \n" + imageDownloadLink;
    }

    public boolean isFitForChildren() {
        return fitForChildren;
    }

    public void setFitForChildren(boolean fitForChildren) {
        this.fitForChildren = fitForChildren;
    }

    public boolean isFitForGuarding() {
        return fitForGuarding;
    }

    public void setFitForGuarding(boolean fitForGuarding) {
        this.fitForGuarding = fitForGuarding;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String isFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        if (favorite.equals("false") || favorite.equals("true"))
                this.favorite = favorite;
    }

    public String isSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
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

    public void setImageDownloadLink(String imageDownloadLink) {
        this.imageDownloadLink = imageDownloadLink;
    }

    public String getImageDownloadLink() {
        return imageDownloadLink;
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
