package com.fts.e_commerce.repository;
import com.fts.e_commerce.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    AdminEntity findByEmail(String email);
}
