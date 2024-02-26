package com.spring.eStore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;
    @Size(min=3, max=30, message="Min 3 character and Max 30 character only")
    private String name;
    @Pattern(regexp="^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message="Invalid Email")
    @NotBlank(message = "Email Required")
    private String email;
    @NotBlank(message = "Password required")
    private String password;
    @NotBlank(message="gender required")
    private String gender;
    private String about;
    private String imageName;
    private Set<RoleDto> roles = new HashSet<>();
}
