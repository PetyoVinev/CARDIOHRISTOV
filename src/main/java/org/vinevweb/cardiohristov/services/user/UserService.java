package org.vinevweb.cardiohristov.services.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    boolean registerUser(UserServiceModel userServiceModel);

    boolean editUser(UserServiceModel userServiceModel);

    UserServiceModel extractUserByEmail(String email);

    UserServiceModel extractUserById(String id);

    List<UserServiceModel> extractAllUsersOrderedAlphabetically();

    boolean editUserRole(String email, String role);

    void deleteProfile(UserServiceModel userServiceModel);
}
