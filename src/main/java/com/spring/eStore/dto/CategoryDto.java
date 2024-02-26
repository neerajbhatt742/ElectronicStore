package com.spring.eStore.dto;

import com.spring.eStore.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String categoryId;
    @NotBlank(message = "Title required !!")
    @Size(min = 4, message="Title must be minimum 4 characters")
    private String title;
    @NotBlank(message = "Description required !!")
    private String description;
    private String coverImage;
}
