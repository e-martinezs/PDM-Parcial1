package com.example.parcial1;

import java.io.Serializable;
import java.util.Date;

public class Contact implements Serializable{
    private String name;
    private String lastName;
    private String id;
    private String email;
    private String address;
    private String phone;
    private Date date;
    private boolean favorite = false;
    private int imageId;

    public Contact(String name, String lastName, String id, String phone, String email, String address, int imageId) {
        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
