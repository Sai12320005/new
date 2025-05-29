package com.fts.e_commerce.service;
import com.fts.e_commerce.entity.UserEntity;
import com.fts.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class UserService {

    private  final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity saveUser(UserEntity user) {
        // Optional: Add validation logic here if needed before saving
        return userRepository.save(user);
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
