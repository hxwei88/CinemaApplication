package com.example.cinemaapplication;

public class Model {

    private int image;
    private String title;
    private String desc;
    private String genre;
    private String path;
    private double price;

    public Model(int image, String title, String desc, String genre, double price, String path) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.genre = genre;
        this.price = price;
        this.path = path;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
