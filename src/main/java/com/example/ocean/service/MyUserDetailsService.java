package com.example.ocean.service;

import com.example.ocean.model.UserPrincipal;
import com.example.ocean.model.Users;
import com.example.ocean.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=userRepo.findByUsername(username);
        System.out.println(user.getUsername());
        if(user == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found");
        }else {
            return new UserPrincipal(user);
        }
    }
}
