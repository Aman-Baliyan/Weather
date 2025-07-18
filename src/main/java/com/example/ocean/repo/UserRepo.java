package com.example.ocean.repo;


import com.example.ocean.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;


public interface UserRepo extends JpaRepository<Users,Integer> {

   @Query
    public Users findByEmail(String email);
}
