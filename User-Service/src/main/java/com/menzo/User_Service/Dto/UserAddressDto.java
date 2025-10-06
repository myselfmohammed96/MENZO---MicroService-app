package com.menzo.User_Service.Dto;

public class UserAddressDto {

    private Long id;
    private String unitAddress;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;

    public UserAddressDto() {}

    public UserAddressDto(Long id, String unitAddress, String street,
                          String landmark, String city, String state,
                          String country, String pincode) {
        this.id = id;
        this.unitAddress = unitAddress;
        this.street = street;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String toString() {
        return "\nUserAddressDto:\nid: " + id + "\nunitAddress: " + unitAddress +
                "\nstreet: " + street + "\nlandmark: " + landmark + "\ncity: " +
                city + "\nstate: " + state + "\ncountry: " + country + "\npincode: " +
                pincode + "\n";
    }

}
