package com.cache.bloomfilter.repository;

import com.cache.bloomfilter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);

    @Query("SELECT u.userName FROM User u")
    List<String> findAllUserNames();
}