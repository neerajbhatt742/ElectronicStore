package com.spring.eStore.repository;

import com.spring.eStore.entity.Cart;
import com.spring.eStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {
   Optional<Cart> findByUser(User user);
}
