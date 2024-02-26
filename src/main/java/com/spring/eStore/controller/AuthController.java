package com.spring.eStore.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.spring.eStore.dto.JwtRequest;
import com.spring.eStore.dto.JwtResponse;
import com.spring.eStore.dto.UserDto;
import com.spring.eStore.entity.User;
import com.spring.eStore.exception.BadApiRequest;
import com.spring.eStore.security.JwtHelper;
import com.spring.eStore.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
@Tag(name="AuthController", description = "Apis for Auth Authentication")
@SecurityRequirement(name="scheme1")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
    @Value("${googleClientId}")
    private String googleClientId;
    @Value("${newPassword}")
    private String newPassword;

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name),UserDto.class), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
     this.doAuthenticate(request.getEmail(),request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtHelper.generateToken(userDetails);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    //login with google api
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {
        // get idToken
        String idToken = data.get("idToken").toString();
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        GsonFactory defaultInstance = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, defaultInstance).setAudience(Collections.singleton(googleClientId));
        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(),idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        log.info("payload:{}",payload);
        String email = payload.getEmail();

        User user = userService.findUserByEmailOptional(email).orElse(null);

        if(user == null) {
           user =  this.saveUser(email,data.get("name").toString());
        }
        ResponseEntity<JwtResponse> jwtResponse = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
        return jwtResponse;
    }

    private User saveUser(String email, String name) {
        UserDto newUser = UserDto.builder()
                .email(email)
                .name(name)
                .roles(new HashSet<>())
                .password(newPassword)
                .build();
        UserDto userDto = userService.creatUser(newUser);
        return modelMapper.map(userDto,User.class);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);
        try{
            manager.authenticate(authenticationToken);
        } catch(BadCredentialsException ex) {
            throw new BadApiRequest("Invalid username or password");
        }
    }
}
