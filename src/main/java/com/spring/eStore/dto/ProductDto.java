package com.spring.eStore.dto;

import com.spring.eStore.entity.Category;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    private String productId;
    private String title;
    private String description;
    private int price;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private int discountedPrice;
    private String productImageName;
    private CategoryDto category;
}
