package com.example.nalone.objects;

public class AddressSearch {

    private String address;
    private Double latitude;
    private Double longitude;
    private String city;

    public AddressSearch(String address, Double latitude, Double longitude, String city) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }
}
