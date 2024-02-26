package com.spring.eStore.controller;

import com.spring.eStore.dto.ApiResponseMessage;
import com.spring.eStore.dto.ImageResponse;
import com.spring.eStore.dto.PageableResponse;
import com.spring.eStore.dto.UserDto;
import com.spring.eStore.service.FileService;
import com.spring.eStore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name="UserController", description = "API for operations related to user")
@SecurityRequirement(name="scheme1")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    //create
    @PostMapping
    @Operation(summary="create new user", description = "user creation api")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Success | OK"),
            @ApiResponse(responseCode="401", description="Not Authorized"),
            @ApiResponse(responseCode="201", description="new user created"),
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto userDto1 = userService.creatUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId,@Valid @RequestBody UserDto userDto){
        UserDto updatedUserDto = userService.updateUser(userDto,userId);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException {
        userService.delete(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User Deleted")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    //get all
    @GetMapping
    @Operation(summary="Get All Users", description = "get all user api")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value="pageNumber",defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10", required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "name", required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc", required = false) String sortDir
    ) 
    {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    //get single
    @GetMapping("/{userId}")
    @Operation(summary="Get user by Id")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }
    //get email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    //search
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords) {
        return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
    }

    //upload user profile image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile userImage, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(userImage,imageUploadPath);
        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto newUserDto = userService.updateUser(user,userId);
        ImageResponse imageResponse = ImageResponse.builder()
                                        .imageName(imageName)
                                        .success(true)
                                        .status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //serve user profile image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto userDto = userService.getUserById(userId);
        log.info("User image name {}",userDto.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath,userDto.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
}
