package com.spring.eStore.service;

import com.spring.eStore.dto.CategoryDto;
import com.spring.eStore.dto.PageableResponse;
import com.spring.eStore.dto.ProductDto;

import java.util.List;

public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);
    //update
    ProductDto update (ProductDto productDto, String productId);
    // delete
    void delete(String productId);
    // get single
    ProductDto get(String productId);
    // getall
    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    // getall:live
    PageableResponse<ProductDto>  getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
    //search product
    PageableResponse<ProductDto>  searchByTitle(String substring, int pageNumber, int pageSize, String sortBy, String sortDir);

    //create product with category
    ProductDto createProductWithCategory(ProductDto productDto, String CategoryId);

    ProductDto updateProductCategory(String productId, String categoryId);

    PageableResponse<ProductDto> getAllByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
