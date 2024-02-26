package com.spring.eStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.eStore.dto.UserDto;
import com.spring.eStore.entity.Role;
import com.spring.eStore.entity.User;
import com.spring.eStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;
    private Role role;
    private User user;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

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
    }
    //create user method
    @Test
    public void createUserTest() throws Exception {
       //req // call /user + post + user data as json
       //res // return json and status code
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.creatUser(Mockito.any())).thenReturn(userDto);
        //req for url
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }

    private String convertObjectToJson(Object user) {
        String s = null;
        try {
           s = new ObjectMapper().writeValueAsString(user);
        } catch(Exception ex)  {
            ex.printStackTrace();
        }
        return s;
    }

}
