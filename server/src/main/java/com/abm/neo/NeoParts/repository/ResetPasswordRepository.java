package com.abm.neo.NeoParts.repository;

import com.abm.neo.NeoParts.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswordToken, String> {

    ResetPasswordToken findFirstByEmailOrderByCreatedDateDesc(String email);
}
