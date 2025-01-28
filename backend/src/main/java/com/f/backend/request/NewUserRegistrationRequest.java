package com.f.backend.request;

import java.sql.Date;

import com.f.backend.enums.Gender;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRegistrationRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Date of birth is mandatory")
    private Date dob;

    @NotNull(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10,13}$", message = "Invalid phone number")
    private String phone;

    @NotNull(message = "Gender is mandatory")
    private Gender gender;
}