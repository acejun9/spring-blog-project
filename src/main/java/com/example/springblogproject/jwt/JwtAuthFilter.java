package com.example.springblogproject.jwt;

import com.example.springblogproject.dto.MsgStatusResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.springblogproject.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.resolveAccessToken(request);

        if(!accessToken.equals("error")){
            if(!jwtUtil.validateToken(accessToken)){
                String refreshToken = jwtUtil.resolveRefreshToken(request);
                if(!jwtUtil.validateToken(refreshToken)) {
                    jwtExceptionHandler(response, "Refresh Token Error", HttpStatus.UNAUTHORIZED);
                    return;
                }
            }
            Claims info = jwtUtil.getUserInfoFromToken(accessToken);
            setAuthentication(info.getSubject());
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String username){
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus httpStatus) {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new MsgStatusResponseDto(msg,httpStatus));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Authentication createAuthentication(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
