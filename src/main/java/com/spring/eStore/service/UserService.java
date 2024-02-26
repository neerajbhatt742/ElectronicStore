package com.spring.eStore.service;

import com.spring.eStore.dto.PageableResponse;
import com.spring.eStore.dto.UserDto;
import com.spring.eStore.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    //create
    UserDto creatUser(UserDto userDto);
    //update
    UserDto updateUser(UserDto userDto,String userId);
    //delete
    void delete(String userId) throws IOException;
    //get all User
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    // get single user by id
    UserDto getUserById(String userId);
    // get single user by email
    UserDto getUserByEmail(String email);
    // search user
    List<UserDto> searchUser(String keyword);
    Optional<User> findUserByEmailOptional(String email);
}
