package com.example.udomime;

public class Animal {

    // Store the id of the  movie poster
    private int idAnimal;
    // Store the name of the movie
    private String name;
    // Store the release date of the movie
    private String breed;
    private String shelter;
    private String url;

    // Constructor that is used to create an instance of the Movie object
    public Animal(int id, String name, String breed, String url, String shelter) {
        this.idAnimal = id;
        this.name = name;
        this.breed = breed;
        this.url = url;
        this.shelter = shelter;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShelter() {
        return shelter;
    }

    public void setShelter(String shelter) {
        this.shelter = shelter;
    }
}
