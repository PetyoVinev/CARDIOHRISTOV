package org.vinevweb.cardiohristov.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.errors.IdNotFoundException;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.repositories.UserRoleRepository;
import org.vinevweb.cardiohristov.services.CommentServiceImpl;
import org.vinevweb.cardiohristov.services.LogService;
import org.vinevweb.cardiohristov.services.user.UserServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTests {

    private User fakeUser;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private CommentServiceImpl commentService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LogService logService;

    @Before
    public void createFakeUser() {
        when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("fakeHash");
        this.fakeUser = new User();
        fakeUser.setId("0c39a7b4-b039-4d78-a0f8-333be7b0e718");
        fakeUser.setFirstName("Fake");
        fakeUser.setLastName("Fakes");
        fakeUser.setUsername("fake@fake.bg");
        fakeUser.setPassword(this.bCryptPasswordEncoder.encode("1111"));
        fakeUser.setAuthorities(new HashSet<>());
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetails() {
        when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.fakeUser));

        UserDetails userDetails = this.userService.loadUserByUsername(this.fakeUser.getUsername());

        Assert.assertEquals("", this.fakeUser.getUsername(), userDetails.getUsername());

        Mockito.verify(this.userRepository).findByUsername("fake@fake.bg");
    }

    @Test
    public void extractUserByEmailShouldReturnUser() {
        ModelMapper modelMapper = new ModelMapper();

        when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.fakeUser, UserServiceModel.class));
        when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.fakeUser));

        UserServiceModel userServiceModel = this.userService.extractUserByEmail("fake@fake.bg");

        Assert.assertEquals("", this.fakeUser.getUsername(), userServiceModel.getEmail());

        Mockito.verify(this.userRepository).findByUsername("fake@fake.bg");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void extractUserByNonexistentEmailShouldThrowException() {
        this.userService.extractUserByEmail("fake@fake.bg");
        Mockito.verify(this.userRepository).findByUsername("fake@fake.bg");
    }

    @Test
    public void registerUserShouldReturnTrue() {
        ModelMapper modelMapper = new ModelMapper();

        UserServiceModel userServiceModel = modelMapper.map(this.fakeUser, UserServiceModel.class);
        userServiceModel.setEmail(this.fakeUser.getUsername());

        when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(this.fakeUser);
        when(this.userRepository.save(this.fakeUser)).thenReturn(this.fakeUser);

        boolean result = this.userService.registerUser(userServiceModel);

        Assert.assertTrue(result);
    }

    @Test
    public void extractUserByIdShouldReturnUser() {
        ModelMapper modelMapper = new ModelMapper();
        when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.fakeUser, UserServiceModel.class));
        when(this.userRepository.findById(Mockito.any())).thenReturn(Optional.of(this.fakeUser));

        UserServiceModel userServiceModel = this.userService.extractUserById("0c39a7b4-b039-4d78-a0f8-333be7b0e718");

        Assert.assertEquals("", "0c39a7b4-b039-4d78-a0f8-333be7b0e718", userServiceModel.getId());

        Mockito.verify(this.userRepository).findById("0c39a7b4-b039-4d78-a0f8-333be7b0e718");
    }

    @Test(expected = IdNotFoundException.class)
    public void extractUserByIdShouldThrowException() {
        this.userService.extractUserById("0c39a7b4-b039-4d78-a0f8-333be7b0e718");
        Mockito.verify(this.userRepository).findById("0c39a7b4-b039-4d78-a0f8-333be7b0e718");
    }

    @Test
    public void editUserShouldReturnTrue() {
        ModelMapper modelMapper = new ModelMapper();
        UserServiceModel userServiceModel = modelMapper.map(this.fakeUser, UserServiceModel.class);

        when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(this.fakeUser));
        userServiceModel.setEmail(this.fakeUser.getUsername());

        boolean result = this.userService.editUser(userServiceModel);

        Assert.assertTrue("", result);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void editNonExistentUserShouldThrowException() {
        ModelMapper modelMapper = new ModelMapper();
        UserServiceModel userServiceModel = modelMapper.map(this.fakeUser, UserServiceModel.class);
        this.userService.editUser(userServiceModel);
        Mockito.verify(this.userRepository).findByUsername("fake@fake.bg");
    }

    @Test
    public void editUserRoleShouldReturnTrue() {
        ModelMapper modelMapper = new ModelMapper();
        UserServiceModel userServiceModel = modelMapper.map(this.fakeUser, UserServiceModel.class);
        when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(this.fakeUser));

        userServiceModel.setEmail(this.fakeUser.getUsername());

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        when(auth.getPrincipal()).thenReturn(this.fakeUser);

        when(logService.addEvent(Mockito.any())).thenReturn(true);

        boolean result = this.userService.editUserRole(userServiceModel.getEmail(), "moderator");

        Assert.assertTrue(result);
    }

    @Test
    public void deleteUserRemovesItFromDBAndCreatesLog() {

        User user  = new User();
        user.setUsername("boko@abv.bg");
        user.setComments(new HashSet<>());
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        user.getComments().add(comment1);
        user.getComments().add(comment2);

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setId("1234");

        Mockito.when(this.userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(new User());

        this.userService.deleteProfile(userServiceModel);

        verify(userRepository, times(2))
                .saveAndFlush(user);

        verify(commentService)
                .removeCommentFromArticleAndDelete(comment1);
        verify(commentService)
                .removeCommentFromArticleAndDelete(comment2);

        verify(userRepository)
                .deleteById("1234");
        verify(logService)
                .addEvent(any());



    }
    @Test
    public void extractAllUsersOrderedAlphabeticallyWorksCorrectly() {


        List<User> users = new ArrayList<>();
        users.add(this.fakeUser);

        Mockito.when(this.userRepository.findAllByOrderByUsernameAsc()).thenReturn(users);
        when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(new UserServiceModel());


        List<UserServiceModel> result = this.userService.extractAllUsersOrderedAlphabetically();

        verify(userRepository).findAllByOrderByUsernameAsc();

        assertEquals(users.stream()
                .map(u -> {
                    UserServiceModel userServiceModel = this.modelMapper.map(u, UserServiceModel.class);
                    userServiceModel.setEmail(u.getUsername());

                    return userServiceModel;
                }).collect(Collectors.toList()), result);

    }

}