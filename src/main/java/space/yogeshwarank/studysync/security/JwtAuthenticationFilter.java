package space.yogeshwarank.studysync.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            doFilter(request, response,filterChain);
            return;
        }

        try{
            String token = header.substring(7);

            if(jwtService.isTokenValid(token)){

                String userId = jwtService.extractUserId(token);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        catch(Exception exception){
            SecurityContextHolder.clearContext();
        }

        doFilter(request, response, filterChain);
    }
}
