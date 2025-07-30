package ro.tuc.ds2020.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupRequestDto {

    private String name;

    private String address;

    private int age;

   /* @Pattern(regexp = "^(?=.[A-Z])(?=.[a-z])(?=.\\d)(?=.[@#$%^&+=!*()_])[A-Za-z\\d@#$%^&+=!*()_]{8,}$",
            message = "Password must start with an uppercase letter, contain lowercase letters, at least one digit, at least one special character, and have a minimum length of 8 characters.")*/
    private String password;


}