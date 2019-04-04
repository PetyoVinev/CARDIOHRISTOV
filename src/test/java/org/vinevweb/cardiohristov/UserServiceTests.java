package org.vinevweb.cardiohristov;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.errors.IdNotFoundException;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.repositories.UserRoleRepository;
import org.vinevweb.cardiohristov.services.user.UserServiceImpl;

import java.util.Optional;

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
    private UserRoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void createFakeUser() {
        Mockito.when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("fakeHash");
        this.fakeUser = new User();
        fakeUser.setId("0c39a7b4-b039-4d78-a0f8-333be7b0e718");
        fakeUser.setFirstName("Fake");
        fakeUser.setLastName("Fakes");
        fakeUser.setUsername("fake@fake.bg");
        fakeUser.setPassword(this.bCryptPasswordEncoder.encode("1111"));
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetails() {
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.fakeUser));

        UserDetails userDetails = this.userService.loadUserByUsername(this.fakeUser.getUsername());

        Assert.assertEquals("", this.fakeUser.getUsername(), userDetails.getUsername());

        Mockito.verify(this.userRepository).findByUsername("fake@fake.bg");
    }

    @Test
    public void extractUserByEmailShouldReturnUser() {
        ModelMapper modelMapper = new ModelMapper();

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.fakeUser, UserServiceModel.class));
        Mockito.when(this.userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(this.fakeUser));

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

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(this.fakeUser);
        Mockito.when(this.userRepository.save(this.fakeUser)).thenReturn(this.fakeUser);

        boolean result = this.userService.registerUser(userServiceModel);

        Assert.assertTrue(result);
    }

    @Test
    public void extractUserByIdShouldReturnUser() {
        ModelMapper modelMapper = new ModelMapper();
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(modelMapper.map(this.fakeUser, UserServiceModel.class));
        Mockito.when(this.userRepository.findById(Mockito.any())).thenReturn(Optional.of(this.fakeUser));

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

        Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(this.fakeUser));
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
        Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(this.fakeUser));

        userServiceModel.setEmail(this.fakeUser.getUsername());

        boolean result = this.userService.editUserRole(userServiceModel.getEmail(), "fakeRole");

        Assert.assertTrue("", result);
    }
}
