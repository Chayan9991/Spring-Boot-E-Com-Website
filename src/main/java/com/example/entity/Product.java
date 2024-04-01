package com.example.entity;

public class Product {
    private int id;
    private String imgUrl;
    private String name;
    private int price ;
    private String description;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }



    public Product(int id, String imgUrl, String name, int price, String description) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.name = name;
        this.price = price;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
