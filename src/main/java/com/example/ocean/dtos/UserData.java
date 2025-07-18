package com.example.ocean.dtos;

import com.example.ocean.model.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.catalina.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserData {

    @NotBlank
    @Email
    private String email;

    @NotNull
    private String password;


    public Users to(){
        return  Users
                .builder()
                .email(this.getEmail())
                .password(this.getPassword())
                .build();
    }

}
