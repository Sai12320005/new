package com.fts.e_commerce.service;

import com.fts.e_commerce.entity.AddressEntity;
import com.fts.e_commerce.entity.UserEntity;
import com.fts.e_commerce.repository.AddressRepository;
import com.fts.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    private  final UserRepository userRepository;

    public AddressEntity saveAddress(Long userId, AddressEntity address) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        address.setUser(user);
        return addressRepository.save(address);
    }

    public List<AddressEntity> getAddressesByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        return addressRepository.findByUser(user);
    }

    public boolean deleteAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            return false;
        }
        try {
            addressRepository.deleteById(addressId);
            return true;
        } catch (Exception e) {
            // Optional: log error here
            return false;
        }
    }
}
