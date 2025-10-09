package com.menzo.User_Service.Dto;

public class UserAddressDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String unitAddress;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private boolean isDefault;

    public UserAddressDto() {}

    public UserAddressDto(Long id, String firstName, String lastName,
                          String phoneNumber, String unitAddress, String street,
                          String landmark, String city, String state,
                          String country, String pincode, boolean isDefault) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.unitAddress = unitAddress;
        this.street = street;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.isDefault = isDefault;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String toString() {
        return "\nUserAddressDto:\nid: " + id + "\nfirstName: " + firstName + "\nlastName: " +
                lastName + "\nphoneNumber: " + phoneNumber + "\nunitAddress: " + unitAddress +
                "\nstreet: " + street + "\nlandmark: " + landmark + "\ncity: " +
                city + "\nstate: " + state + "\ncountry: " + country + "\npincode: " +
                pincode + "\nisDefault: " + isDefault + "\n";
    }

}
