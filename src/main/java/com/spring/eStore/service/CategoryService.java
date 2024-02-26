package com.spring.eStore.service;

import com.spring.eStore.dto.CategoryDto;
import com.spring.eStore.dto.PageableResponse;

public interface CategoryService {

    //create
    CategoryDto create(CategoryDto categoryDto);
    //update
    CategoryDto update(CategoryDto categoryDto, String categoryId);
    //delete
    void delete(String categoryId);
    // get all
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    //get single category
    CategoryDto get(String categoryId);
    //search
}
