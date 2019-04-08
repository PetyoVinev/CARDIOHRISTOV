package org.vinevweb.cardiohristov.domain.models.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static org.vinevweb.cardiohristov.common.Constants.*;

public class UserRegisterBindingModel {



    private String emailRegister;
    private String passwordRegister;
    private String confirmPassword;
    private String firstName;
    private String lastName;

    public UserRegisterBindingModel() {
    }

    @NotNull(message = USER_EMAIL_COULD_NOT_BE_NULL)
    @NotEmpty(message = USER_EMAIL_COULD_NOT_BE_EMPTY)
    @Pattern(regexp = USER_EMAIL_PATTERN, message = INVALID_EMAIL)
    public String getEmailRegister() {
        return emailRegister;
    }

    public void setEmailRegister(String emailRegister) {
        this.emailRegister = emailRegister;
    }


    @NotNull(message = USER_PASSWORD_COULD_NOT_BE_NULL)
    @NotEmpty(message = USER_PASSWORD_COULD_NOT_BE_EMPTY)
    @Length(min = 4, max = 20, message = USER_PASSWORD_REQUIREMENTS)
    public String getPasswordRegister() {
        return passwordRegister;
    }

    public void setPasswordRegister(String passwordRegister) {
        this.passwordRegister = passwordRegister;
    }


    @NotNull(message = USER_PASSWORD_COULD_NOT_BE_NULL)
    @NotEmpty(message = USER_PASSWORD_COULD_NOT_BE_EMPTY)
    @Length(min = 4, max = 20, message = USER_PASSWORD_REQUIREMENTS)
    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotNull(message = USER_NAME_COULD_NOT_BE_NULL)
    @NotEmpty(message = USER_NAME_COULD_NOT_BE_EMPTY)
    @Length(min = 2, message = USER_NAME_LENGTH_REQUIREMENTS)
    @Pattern(regexp = USER_NAME_PATTERN, message = USER_NAME_REQUIREMENTS)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull(message = USER_FAMILYNAME_COULD_NOT_BE_NULL)
    @NotEmpty(message = USER_FAMILYNAME_COULD_NOT_BE_EMPTY)
    @Length(min = 2, message = USER_FAMILYNAME_LENGTH_REQUIREMENTS)
    @Pattern(regexp = USER_FAMILYNAME_PATTERN, message = USER_FAMILYNAME_REQUIREMENTS)
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
