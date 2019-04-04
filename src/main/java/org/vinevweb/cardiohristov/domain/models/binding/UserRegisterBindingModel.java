package org.vinevweb.cardiohristov.domain.models.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserRegisterBindingModel {

    private String emailRegister;
    private String passwordRegister;
    private String confirmPassword;
    private String firstName;
    private String lastName;

    public UserRegisterBindingModel() {
    }

    @NotNull(message = "Имейлът не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за имайл не може да е празно.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Невалиден имайл.")
    public String getEmailRegister() {
        return emailRegister;
    }

    public void setEmailRegister(String emailRegister) {
        this.emailRegister = emailRegister;
    }


    @NotNull(message = "Паролата не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за паролата не може да е празно.")
    @Length(min = 4, max = 20, message = "Паролата трябва да е с дължина между 4 и 20 символа.")
    public String getPasswordRegister() {
        return passwordRegister;
    }

    public void setPasswordRegister(String passwordRegister) {
        this.passwordRegister = passwordRegister;
    }


    @NotNull(message = "Паролата не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за паролата не може да е празно.")
    @Length(min = 4, max = 20, message = "Паролата трябва да е с дължина между 4 и 20 символа.")
    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotNull(message = "Името не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за името не може да е празно.")
    @Length(min = 2, message = "Името трябва да съдържа минимум 2 букви.")
    @Pattern(regexp = "^[A-Z][a-zA-Z]+", message = "Името трябва да започва с главна буква, и да е изписано с латински букви.")
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull(message = "Фамилията не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за фамилия не може да е празно.")
    @Length(min = 2, message = "Фамилията трябва да съдържа минимум 2 букви.")
    @Pattern(regexp = "^[A-Z][a-zA-Z]+", message = "Фамилията трябва да започва с главна буква, и да е изписана с латински букви.")
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
