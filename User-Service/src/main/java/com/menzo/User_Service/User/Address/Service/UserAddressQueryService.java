package com.menzo.User_Service.User.Address.Service;

import com.menzo.User_Service.User.Address.Dto.UserAddressDto;
import com.menzo.User_Service.User.Address.Entity.UserAddress;
import com.menzo.User_Service.User.Address.Repository.UserAddressRepository;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressQueryService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserAddressRepository addressRepo;



    /*
    *
    *   Get all user address
    *
    */
    public List<UserAddressDto> getAllAddressByEmail(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        List<UserAddress> userAddresses = addressRepo.findByUser(user);
        if (userAddresses == null) {
            throw new EntityNotFoundException("User has no addresses");
        }
        List<UserAddressDto> userAddressDtos = userAddresses.stream()
                .map(userAddress -> {
                    return UserAddressDto.builder()
                            .id(userAddress.getUserAddressId())
                            .firstName(userAddress.getFirstName())
                            .lastName(userAddress.getLastName())
                            .phoneNumber(userAddress.getPhoneNumber())
                            .unitAddress(userAddress.getAddress().getUnitAddress())
                            .street(userAddress.getAddress().getStreet())
                            .landmark(userAddress.getAddress().getLandmark())
                            .city(userAddress.getAddress().getCity())
                            .state(userAddress.getAddress().getState())
                            .country(userAddress.getAddress().getCountry().getCountryName())
                            .pincode(userAddress.getAddress().getPincode())
                            .isDefault(userAddress.isDefault())
                            .build();
                }).collect(Collectors.toList());
        return userAddressDtos;
    }



    /*
    *
    *   Get default address of user
    *   User identified by user email
    *
    */
    public UserAddressDto getDefaultAddressByEmail(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        List<UserAddress> userAddresses = addressRepo.findByUser(user);
        if (userAddresses == null) {
            throw new EntityNotFoundException("User has no addresses");
        }
        UserAddress defaultAddress = userAddresses.stream()
                .filter(userAddress -> userAddress.isDefault())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User doesn't have default address"));

        return UserAddressDto.builder()
                .id(defaultAddress.getUserAddressId())
                .firstName(defaultAddress.getFirstName())
                .lastName(defaultAddress.getLastName())
                .phoneNumber(defaultAddress.getPhoneNumber())
                .unitAddress(defaultAddress.getAddress().getUnitAddress())
                .street(defaultAddress.getAddress().getStreet())
                .landmark(defaultAddress.getAddress().getLandmark())
                .city(defaultAddress.getAddress().getCity())
                .state(defaultAddress.getAddress().getState())
                .country(defaultAddress.getAddress().getCountry().getCountryName())
                .pincode(defaultAddress.getAddress().getPincode())
                .isDefault(defaultAddress.isDefault())
                .build();
    }
}
