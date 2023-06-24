package com.pet.shelter.friends.pets.filtering;

public class FilterData {
    private String category;
    private String sex;
    private String size;
    private String age;
    private String friendly;
    private String guarding;


    public FilterData() {
    }

    public FilterData(String category, String sex, String size, String age, String friendly, String guarding) {
        this.category = category;
        this.sex = sex;
        this.size = size;
        this.age = age;
        this.friendly = friendly;
        this.guarding = guarding;
    }

    public String getCategory() {
        return category;
    }

    public String getSex() {
        return sex;
    }

    public String getSize() {
        return size;
    }

    public String getAge() {
        return age;
    }

    public String getFriendly() {
        return friendly;
    }

    public String getGuarding() {
        return guarding;
    }
}
