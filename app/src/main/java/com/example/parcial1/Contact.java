package com.example.parcial1;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Contact implements Parcelable {
    public static Uri defaultUri;
    private String name;
    private String lastName;
    private String id;
    private String email;
    private String address;
    private ArrayList<String> phones;
    private String imageUri;
    private String date;
    private boolean favorite = false;

    public Contact(String name, String lastName, String id, ArrayList<String> phones, String email, String address, String date, String imageUri) {
        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.phones = phones;
        this.email = email;
        this.address = address;
        this.date = date;
        this.imageUri = imageUri;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        lastName = in.readString();
        id = in.readString();
        email = in.readString();
        address = in.readString();
        phones = new ArrayList<>();
        date = in.readString();
        in.readList(phones, null);
        imageUri = in.readString();
        favorite = in.readByte() != 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(lastName);
        parcel.writeString(id);
        parcel.writeString(email);
        parcel.writeString(address);
        parcel.writeList(phones);
        parcel.writeString(date);
        parcel.writeString(imageUri);
        parcel.writeByte((byte) (favorite ? 1 : 0));
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
