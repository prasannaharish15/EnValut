package com.myvalut.envalut_backend.filter;

import com.myvalut.envalut_backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            final String authHeader=request.getHeader("Authorization");
            String email=null;
            String token=null;
            if(authHeader != null &&authHeader.startsWith("Bearer ")){
                token=authHeader.substring(7);
                email=jwtUtil.getSubject(token);
            }
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                if(jwtUtil.validToken(token,email)){
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(email,null, new ArrayList<>());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request,response);

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
