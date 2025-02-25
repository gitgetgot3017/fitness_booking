package com.lhj.FitnessBooking.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhj.FitnessBooking.jwt.JwtService;
import com.lhj.FitnessBooking.jwt.exception.NotExistAccessTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
//            String authorization = request.getHeader("Authorization");
//            if (authorization == null) {
//                throw new NotExistAccessTokenException("로그인이 필요합니다.");
//            }
//            String accessToken = authorization.replace("Bearer ", "");
//
//            Claims claims = jwtService.getClaims(accessToken);
//
//            String memberNum = (String) claims.get("memberNum");
//            request.setAttribute("memberNum", memberNum);
            return true;

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"ACCESS_TOKEN_EXPIRED\"}");
            return false;
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "ACCESS_TOKEN_INVALID");
            errorResponse.put("classType", e.getClass().toString());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(jsonErrorResponse);

            return false;
        }
    }
}
