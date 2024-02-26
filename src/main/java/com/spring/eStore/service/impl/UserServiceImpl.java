package com.spring.eStore.service.impl;

import com.spring.eStore.dto.PageableResponse;
import com.spring.eStore.dto.UserDto;
import com.spring.eStore.entity.Role;
import com.spring.eStore.entity.User;
import com.spring.eStore.exception.BadApiRequest;
import com.spring.eStore.exception.ResourceNotFoundException;
import com.spring.eStore.helper.Helper;
import com.spring.eStore.repository.RoleRepository;
import com.spring.eStore.repository.UserRepository;
import com.spring.eStore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Value("${user.profile.image.path}")
    private String imagePath;
    @Value("${normal.role.id}")
    private String normaRoleId;
    @Override
    public UserDto creatUser(UserDto userDto) {
        //generate user id
        log.info("Creating new user");
//        Optional<User> isUserExist = userRepository.findByEmail(userDto.getEmail());
//        if(isUserExist != null) {
//            throw new BadApiRequest("Email Already Exists!!");
//        }
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = dtoToEntity(userDto);
        Role role = roleRepository.findById(normaRoleId).get();
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        UserDto newUserDto = entityToDto(savedUser);
        return newUserDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
       User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!!"));
       user.setName(userDto.getName());
       user.setAbout(userDto.getAbout());
       user.setGender(userDto.getGender());
       user.setPassword(userDto.getPassword());
       user.setImageName(userDto.getImageName());
       User updatedUser = userRepository.save(user);
       UserDto updatedDto = entityToDto(updatedUser);
      return updatedDto;
    }

    @Override
    public void delete(String userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!!"));
        String userImageName = user.getImageName();
        String imageFullPath = imagePath + userImageName;
        try {
            Path path = Paths.get(imageFullPath);
            Files.delete(path);
        }catch(NoSuchFileException ex) {
            log.info("user does not have image");
        }
        user.setRoles(null);
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page,UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!!"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
       List<User> users = userRepository.findByNameContaining(keyword);
       List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }


    private UserDto entityToDto(User savedUser) {
//      UserDto userDto =  UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName()).build();
//
//        return userDto;
        return mapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getGender())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName()).build();
//        return user;
       return mapper.map(userDto,User.class);
    }
}
