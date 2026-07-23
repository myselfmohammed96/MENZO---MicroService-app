package com.menzo.User_Service.User.Address.Service;

import com.menzo.User_Service.Exceptions.DuplicateEntityException;
import com.menzo.User_Service.User.Address.Dto.UserAddressDto;
import com.menzo.User_Service.User.Address.Entity.Address;
import com.menzo.User_Service.User.Address.Entity.Country;
import com.menzo.User_Service.User.Address.Entity.UserAddress;
import com.menzo.User_Service.User.Address.Repository.AddressRepository;
import com.menzo.User_Service.User.Address.Repository.CountryRepository;
import com.menzo.User_Service.User.Address.Repository.UserAddressRepository;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserAddressCommandService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private UserAddressRepository userAddressRepo;


    /*
     *
     *   Add new user address
     *
     */
    public Long addUserAddress(String userEmail, UserAddressDto userAddress) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        Country country = countryRepo.findByCountryNameIgnoreCase(userAddress.getCountry())
                .orElseThrow(() -> new EntityNotFoundException("Country not found with name: " + userAddress.getCountry()));

        Address address = addressRepo.findByUnitAddressIgnoreCaseAndStreetIgnoreCaseAndLandmarkIgnoreCaseAndCityIgnoreCaseAndStateIgnoreCaseAndCountryAndPincode(
                userAddress.getUnitAddress(),
                userAddress.getStreet(),
                userAddress.getLandmark(),
                userAddress.getCity(),
                userAddress.getState(),
                country,
                userAddress.getPincode()
        ).orElseGet(() -> {
            return addressRepo.save(Address.builder()
                    .unitAddress(userAddress.getUnitAddress())
                    .street(userAddress.getStreet())
                    .city(userAddress.getCity())
                    .state(userAddress.getState())
                    .country(country)
                    .pincode(userAddress.getPincode())
                    .landmark(userAddress.getLandmark())
                    .build()
            );
        });
        if (userAddressRepo.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhoneNumberAndUserAndAddress(
                userAddress.getFirstName(),
                userAddress.getLastName(),
                userAddress.getPhoneNumber(),
                user,
                address
        )) {
            throw new DuplicateEntityException("User Address already exists.");
        }
        UserAddress savedUserAddress = userAddressRepo.save(UserAddress.builder()
                .firstName(userAddress.getFirstName())
                .lastName(userAddress.getLastName())
                .phoneNumber(userAddress.getPhoneNumber())
                .user(user)
                .address(address)
                .isDefault(userAddress.isDefault())
                .build()
        );
        return savedUserAddress.getUserAddressId();
    }


    /*
     *
     *   Update user address
     *
     */
    public Long updateUserAddress(String userEmail, Long addressId, UserAddressDto latestUserAddress) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        List<UserAddress> userAddresses = userAddressRepo.findByUser(user);
        if (userAddresses == null) {
            throw new EntityNotFoundException("User has no addresses.");
        }
        UserAddress matchedUserAddress = userAddresses.stream()
                .filter((userAddress) -> Objects.equals(userAddress.getUserAddressId(), addressId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Address doesn't exist with user: " + user.getEmail()));

        Address matchedAddress = matchedUserAddress.getAddress();

        matchedAddress.setUnitAddress(latestUserAddress.getUnitAddress() != null && !latestUserAddress.getUnitAddress().isEmpty() ? latestUserAddress.getUnitAddress() : matchedAddress.getUnitAddress());
        matchedAddress.setStreet(latestUserAddress.getStreet() != null && !latestUserAddress.getStreet().isEmpty() ? latestUserAddress.getStreet() : matchedAddress.getStreet());
        matchedAddress.setLandmark(latestUserAddress.getLandmark() != null && !latestUserAddress.getLandmark().isEmpty() ? latestUserAddress.getLandmark() : matchedAddress.getLandmark());
        matchedAddress.setCity(latestUserAddress.getCity() != null && !latestUserAddress.getCity().isEmpty() ? latestUserAddress.getCity() : matchedAddress.getCity());
        matchedAddress.setState(latestUserAddress.getState() != null && !latestUserAddress.getState().isEmpty() ? latestUserAddress.getState() : matchedAddress.getState());
        matchedAddress.setPincode(latestUserAddress.getPincode() != null && !latestUserAddress.getPincode().isEmpty() ? latestUserAddress.getPincode() : matchedAddress.getPincode());

        Address updatedAddress = addressRepo.save(matchedAddress);

        if (updatedAddress == null) {
            throw new RuntimeException("Update address failed.");
        }
        matchedUserAddress.setFirstName(latestUserAddress.getFirstName() != null && !latestUserAddress.getFirstName().isEmpty() ? latestUserAddress.getFirstName() : matchedUserAddress.getFirstName());
        matchedUserAddress.setLastName(latestUserAddress.getLastName() != null && !latestUserAddress.getLastName().isEmpty() ? latestUserAddress.getLastName() : matchedUserAddress.getLastName());
        matchedUserAddress.setPhoneNumber(latestUserAddress.getPhoneNumber() != null && !latestUserAddress.getPhoneNumber().isEmpty() ? latestUserAddress.getPhoneNumber() : matchedUserAddress.getPhoneNumber());

        if (latestUserAddress.isDefault()) {
            userAddresses.stream()
                    .filter((userAddress) -> userAddress.isDefault())
                    .forEach((userAddress) -> userAddress.setDefault(false));
        }
        matchedUserAddress.setDefault(latestUserAddress.isDefault());

        UserAddress updatedUserAddress = userAddressRepo.save(matchedUserAddress);

        if (updatedUserAddress == null) {
            throw new RuntimeException("Update User address failed");
        }
        return updatedUserAddress.getUserAddressId();
    }


    /*
     *
     *   Delete user address
     *   (soft delete)
     *
     */
    public void deleteUserAddress(String userEmail, Long addressId) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        List<UserAddress> userAddresses = userAddressRepo.findByUser(user);
        if (userAddresses == null) {
            throw new EntityNotFoundException("User has no addresses");
        }
        UserAddress matchedUserAddress = userAddresses.stream()
                .filter((userAddress) -> userAddress.getUserAddressId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Address doesn't exist with user: " + user.getEmail()));

        addressRepo.deleteById(matchedUserAddress.getUserAddressId());
    }
}
