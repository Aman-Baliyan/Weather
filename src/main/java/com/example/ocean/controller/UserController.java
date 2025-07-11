package com.example.ocean.controller;


import com.example.ocean.model.Users;
import com.example.ocean.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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

//    @GetMapping("csrf-token")
//    public CsrfToken getcsrftoken(HttpServletRequest httpServletRequest){
//return (CsrfToken) httpServletRequest.getAttribute("_csrf");
//    }


    @PostMapping("/login")
    public String login(@RequestBody  Users users){
        System.out.println(users);
        return userService.verify(users);
    }



    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userService.register(user);
    }
}
