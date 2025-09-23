package ru.gl.docs.dto;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
public class UserLoginDto {

    @NotBlank
    private String passportNumber;

    @NotBlank
    private String password;

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
