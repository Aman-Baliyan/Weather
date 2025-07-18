package com.example.ocean.service;


import com.example.ocean.model.Users;
import com.example.ocean.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;


    public String register(Users user){
      Users getUser = userRepo.findByEmail(user.getEmail());
      if(getUser == null){
        userRepo.save(user);
        return "success";
      }
      return "user already exists";
    }

    public List<Users> getAlluser() {
        return userRepo.findAll();
    }

    public String verify(Users users) {
        Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(users.getEmail(),users.getPassword()));


       if( authentication.isAuthenticated() ){
           return jwtService.generateToken(users.getEmail());
       }
       return "fail";

    }
}
