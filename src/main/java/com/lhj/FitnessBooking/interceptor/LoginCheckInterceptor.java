package com.lhj.FitnessBooking.interceptor;

import com.lhj.FitnessBooking.member.JwtService;
import com.lhj.FitnessBooking.member.exception.ExpiredAccessTokenException;
import com.lhj.FitnessBooking.member.exception.NotExistAccessTokenException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;

@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new NotExistAccessTokenException("access token이 존재하지 않습니다.");
            }
            String accessToken = authorization.replace("Bearer ", "");

            Claims claims = jwtService.getClaims(accessToken);
            if (claims.getExpiration().before(new Date())) { // 만료된 토큰인 경우
                throw new ExpiredAccessTokenException("access token이 만료되었습니다.");
            }

            String memberNum = (String) claims.get("memberNum");
            request.setAttribute("memberNum", memberNum);
            return true;

        } catch (NotExistAccessTokenException | ExpiredAccessTokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/jsoin");
            response.getWriter().write("{\"accessTokenError\": \"" + e.getMessage() + "\"}");
            return false;
        }
    }
}
