package com.example.ocean.controller;


import com.example.ocean.dtos.UserData;
import com.example.ocean.model.Users;
import com.example.ocean.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173/")
public class UserController {


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    UserService userService;

//    @GetMapping("/hi")
//    public String syahi(HttpServletRequest servletRequest) {
//        return  servletRequest.getSession().getId();
//    }

    @GetMapping("/users")
    public List<Users> getStudentList(){
        return userService.getAlluser();
    }




    @PostMapping("/login")
    public String login(@RequestBody UserData userData){

        Users user = userData.to();
        return userService.verify(user);

    }



    @PostMapping("/register")
    public String register(@RequestBody UserData userData)  {
        try{
            Users user = userData.to();
            user.setPassword(encoder.encode(user.getPassword()));
            return userService.register(user);
        }catch(Exception e){
            return "server error";
        }

    }
}
