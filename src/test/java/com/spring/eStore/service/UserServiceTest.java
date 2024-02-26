package com.spring.eStore.service;

import com.spring.eStore.dto.UserDto;
import com.spring.eStore.entity.Role;
import com.spring.eStore.entity.User;
import com.spring.eStore.exception.ResourceNotFoundException;
import com.spring.eStore.repository.RoleRepository;
import com.spring.eStore.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    ModelMapper mapper;
    User user;
    Role role;
    String roleId;

    @BeforeEach
    public void init() {
        role = Role.builder()
                .roleName("NORMAL")
                .roleId("abc").build();
        user = User.builder()
                .name("Suraj")
                .email("surajjadli@test.com")
                .password("pass")
                .about("test about")
                .roles(Set.of(role))
                .gender("Male").build();
        roleId="abc";
    }

    //Test Create User
    @Test
    public void createUserTest() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.any())).thenReturn(Optional.of(role));
        UserDto userDto = userService.creatUser(mapper.map(user, UserDto.class));
        Assertions.assertNotNull(userDto);
    }

    // Update user test
    @Test
    public void updateUserTestSucess() {
        String userId = "123";
        UserDto userDto = UserDto.builder()
                .name("Suraj Jadli")
                .password("pass")
                .about("test about updated")
                .gender("Male").build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto userDto1 = userService.updateUser(userDto, userId);
        Assertions.assertNotNull(userDto1);
        Assertions.assertEquals("Suraj Jadli",userDto1.getName());
    }

    @Test
    public void updateUserTest_UserNotFound() {
        String userId = "123";
        UserDto userDto = UserDto.builder()
                .name("Suraj Jadli")
                .password("pass")
                .about("test about updated")
                .gender("Male").build();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> userService.updateUser(userDto, userId));
        Assertions.assertEquals("User not found!!", runtimeException.getMessage());
    }

}
