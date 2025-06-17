package com.CptFranck.SportsPeak.config.security.jwt;

import com.CptFranck.SportsPeak.domain.exception.token.InvalidTokenException;
import com.CptFranck.SportsPeak.domain.exception.token.RefreshTokenExpiredException;
import com.CptFranck.SportsPeak.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final TokenService tokenService;

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, TokenService tokenService, UserDetailsService userDetailsService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (bearerToken == null || !bearerToken.startsWith("Bearer ") ||
                    SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = bearerToken.substring(7);

            if (jwtUtils.validateToken(jwt) && tokenService.isTokenValidInStore(jwt)) {
                String username = jwtUtils.extractUsername(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (RefreshTokenExpiredException ex) {
            jwtAuthenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("Token has expired"));
        } catch (InvalidTokenException | IllegalArgumentException ex) {
            jwtAuthenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("Invalid token"));
        }
    }
}
