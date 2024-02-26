package com.spring.eStore.repository;

import com.spring.eStore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface CategoryRepository extends JpaRepository<Category,String> {

}
