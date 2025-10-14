package com.project.authapi.authserviceapi.security;

import com.project.authapi.authserviceapi.entity.User;
import com.project.authapi.authserviceapi.service.CustomUserDetailsService;
import com.project.authapi.authserviceapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Token filter checker
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtServ;
    @Autowired
    private CustomUserDetailsService userDS;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/api/auth/public")
                || path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;

        } else {
            try  {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null && !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String token = authHeader.substring(7);
                String username = jwtServ.extractUsername(token);


                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails currentUD = userDS.loadUserByUsername(username);

                    if (jwtServ.isTokenValid(token, currentUD)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(currentUD, null, currentUD.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

                filterChain.doFilter(request, response);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
        }
    }
}
