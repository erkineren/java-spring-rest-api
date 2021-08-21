package com.erkineren.demo.persistence.repository;


import com.erkineren.demo.persistence.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(@NotBlank String email);

    Optional<User> findByEmail(@NotBlank String email);

}
