package ru.gl.docs.dto;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Component
public class UserRegistrationDto {


    @NotBlank()
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank()
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank()
    @Size(min = 10, max = 10)
    private String passportNumber;

    @NotBlank()
    @Size(min = 6)
    private String password;

    @NotBlank()
    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

