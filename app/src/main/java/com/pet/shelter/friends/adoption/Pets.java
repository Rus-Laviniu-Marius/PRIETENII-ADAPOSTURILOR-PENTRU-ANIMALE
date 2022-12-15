package com.pet.shelter.friends.adoption;

public class Pets {

    private int image_id;
    private String title;
    private int age;
    private int backgroundColor;
    private String size;

    public Pets(int image_id, String title, int age, int backgroundColor, String size) {
        this.image_id = image_id;
        this.title = title;
        this.age = age;
        this.backgroundColor = backgroundColor;
        this.size = size;
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

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
