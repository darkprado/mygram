package com.example.mygram.payload.request;

import com.example.mygram.annotations.PasswordMatches;
import com.example.mygram.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @NotEmpty(message = "Please enter you username")
    private String username;
    @NotEmpty(message = "Please enter you firstname")
    private String firstname;
    @NotEmpty(message = "Please enter you lastname")
    private String lastname;
    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;
    private String confirmPassword;
    @Email(message = "Its should have email format")
    @NotBlank(message = "Email is required")
//    @ValidEmail
    private String email;

}
