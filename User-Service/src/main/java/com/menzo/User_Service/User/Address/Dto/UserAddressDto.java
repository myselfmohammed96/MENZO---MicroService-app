package com.menzo.User_Service.User.Address.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    /////////////////////////////////////

    public String toString() {
        return "\nUserAddressDto:\nid: " + id +
                "\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nphoneNumber: " + phoneNumber +
                "\nunitAddress: " + unitAddress +
                "\nstreet: " + street +
                "\nlandmark: " + landmark +
                "\ncity: " + city +
                "\nstate: " + state +
                "\ncountry: " + country +
                "\ndecode: " + pincode +
                "\nisDefault: " + isDefault + "\n";
    }
}
