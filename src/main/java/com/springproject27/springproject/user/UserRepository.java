package com.springproject27.springproject.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByLastNameContainingIgnoreCase(String lastName);
    List<User> findUsersByEmailContainingIgnoreCase(String email);
    Optional<User> findUserById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndIdNot(String email, Long id);
}
