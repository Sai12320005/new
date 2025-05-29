package com.fts.e_commerce.repository;

import com.fts.e_commerce.entity.AddressEntity;
import com.fts.e_commerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByUser(UserEntity user);
}
