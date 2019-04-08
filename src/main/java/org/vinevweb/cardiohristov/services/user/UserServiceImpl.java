package org.vinevweb.cardiohristov.services.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.vinevweb.cardiohristov.domain.entities.Article;
import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.entities.UserRole;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.errors.IdNotFoundException;
import org.vinevweb.cardiohristov.errors.UserAlreadyExistsException;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.repositories.UserRoleRepository;
import org.vinevweb.cardiohristov.services.CommentService;
import org.vinevweb.cardiohristov.services.LogService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final CommentService commentService;
    private final LogService logService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, CommentService commentService, LogService logService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.commentService = commentService;
        this.logService = logService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = this.userRepository.findByUsername(email).orElse(null);

        if (userDetails == null) {
            throw new UsernameNotFoundException("Wrong or nonexistent email.");
        }

        return userDetails;
    }

    @Override
    public boolean registerUser(UserServiceModel userServiceModel) {


        if (this.userRepository.findByUsername(userServiceModel.getEmail()).orElse(null) != null){
            throw new UserAlreadyExistsException("Registering user " + userServiceModel.getEmail() + " failed. " + "User with that mail already exists!" );
        }

        this.seedRolesInDb();
        User userEntity = this.modelMapper.map(userServiceModel, User.class);
        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setUsername(userServiceModel.getEmail());

        this.setUserRole(userEntity);

        this.userRepository.save(userEntity);
        return true;
    }

    @Override
    public UserServiceModel extractUserByEmail(String email) {
        User userEntity = this.userRepository.findByUsername(email).orElse(null);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Wrong or nonexistent email.");
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userEntity, UserServiceModel.class);
        userServiceModel.setEmail(userEntity.getUsername());

        return userServiceModel;
    }

    @Override
    public UserServiceModel extractUserById(String id) {
        User userEntity = this.userRepository.findById(id).orElse(null);

        if (userEntity == null) {
            throw new IdNotFoundException("Wrong or nonexistent id.");
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userEntity, UserServiceModel.class);
        userServiceModel.setEmail(userEntity.getUsername());

        return userServiceModel;
    }

    @Override
    public boolean editUser(UserServiceModel userServiceModel) {
        User userEntity = this.userRepository.findByUsername(userServiceModel.getEmail()).orElse(null);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Wrong or nonexistent email.");
        }
        userEntity.setFirstName(userServiceModel.getFirstName());
        userEntity.setLastName(userServiceModel.getLastName());

        if (userServiceModel.getPasswordRegister() != ""){
            userEntity.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPasswordRegister()));
        }

        this.userRepository.save(userEntity);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        this.logService.addEvent(new String[]{ LocalDateTime.now().toString(),
                currentUser.getUsername(),
                "Редактиран е потребител с имейл: " + userEntity.getUsername()});

        return true;
    }

    @Override
    public List<UserServiceModel> extractAllUsersOrderedAlphabetically() {
        List<User> userEntities = this.userRepository.findAllOrderedAlphabetically();

        List<UserServiceModel> userServiceModels = userEntities.stream()
                .map(u -> {
                    UserServiceModel userServiceModel = this.modelMapper.map(u, UserServiceModel.class);
                    userServiceModel.setEmail(u.getUsername());

                    return userServiceModel;
                }).collect(Collectors.toList());


        return userServiceModels;
    }

    @Override
    public boolean editUserRole(String email, String role) {
        User userEntity = this.userRepository.findByUsername(email).orElse(null);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Wrong or nonexistent email.");
        }

        this.changeUserRole(userEntity, role);

        this.userRepository.save(userEntity);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        this.logService.addEvent(new String[]{ LocalDateTime.now().toString(),
                currentUser.getUsername(),
                "Променена е ролята на потребител с имейл: " + userEntity.getUsername()});

        return true;
    }

    @Override
    public void deleteProfile(UserServiceModel userServiceModel) {

        User  user  = userRepository.findById(userServiceModel.getId()).orElse(null);

        Set<Comment> comments = new HashSet<>(user.getComments());

        for (Comment comment : comments) {
            user.getComments().remove(comment);
            userRepository.saveAndFlush(user);
            commentService.removeCommentFromArticleAndDelete(comment);
        }

        this.userRepository.deleteById(userServiceModel.getId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        this.logService.addEvent(new String[]{ LocalDateTime.now().toString(),
                currentUser.getUsername(),
                "Изтрит е потребител с имейл: " + user.getUsername()});

    }


    private void seedRolesInDb() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.save(new UserRole("ROLE_ROOT"));
            this.roleRepository.save(new UserRole("ROLE_ADMIN"));
            this.roleRepository.save(new UserRole("ROLE_MODERATOR"));
            this.roleRepository.save(new UserRole("ROLE_USER"));
        }
    }

    private void setUserRole(User userEntity) {
        if (this.userRepository.count() == 0) {
            userEntity.setAuthorities(new HashSet<>(this.roleRepository.findAll()));
        } else {
            UserRole roleUser = this.roleRepository.findByAuthority("ROLE_USER").orElse(null);

            if (roleUser == null) {
                throw new IllegalArgumentException("The role does not exists.");
            }

            userEntity.setAuthorities(new HashSet<>());
            userEntity.getAuthorities().add(roleUser);
        }
    }

    private void changeUserRole(User userEntity, String role) {
        userEntity.getAuthorities().clear();

        switch (role.toLowerCase()) {
            case "admin":
                userEntity.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_ADMIN").orElse(null));
            case "moderator":
                userEntity.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_MODERATOR").orElse(null));
            case "user":
                userEntity.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER").orElse(null));
                break;
        }

    }
}
