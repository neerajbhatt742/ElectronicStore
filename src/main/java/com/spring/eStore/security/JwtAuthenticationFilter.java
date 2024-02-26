package com.spring.eStore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Authorization
        String requestHeader = request.getHeader("Authorization");
        log.info("Header: {}",requestHeader);
        String username = null; // it is email of user that we customized
        String token = null;
        if(requestHeader!=null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);
            try {
                username = jwtHelper.getUsernameFromToken(token);
            }catch (IllegalArgumentException ex){
                log.info("illegalArgumentException {}",ex);
                ex.printStackTrace();
            }catch(ExpiredJwtException ex) {
                log.info("ExpiredJwtException {}",ex);
                ex.printStackTrace();
            }catch(MalformedJwtException ex) {
                log.info("MalformedJwtException {}",ex);
                ex.printStackTrace();
            }catch(Exception ex){
                log.info("Exception {}",ex);
                ex.printStackTrace();
            }
        }else {
            log.info("Invalid token ");
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            ///fetch user
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            //validate token
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if(validateToken){
            //set the authentication
                UsernamePasswordAuthenticationToken  authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                log.info("validation token failed ");
            }
        }
        filterChain.doFilter(request,response);
    }
}
