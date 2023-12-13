package com.mogumogu.spring.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mogumogu.spring.JwtProperties;
import com.mogumogu.spring.PersonEntity;
import com.mogumogu.spring.PrincipalDetails;
import com.mogumogu.spring.repository.AdminRepository;
import com.mogumogu.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Slf4j
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
@Slf4j
@Component
public class GatewayJwtAuthorizationFilter extends AbstractGatewayFilterFactory<GatewayJwtAuthorizationFilter.Config> {
    public static class Config {}
    private AdminRepository adminRepository;
    private UserRepository userRepository;

    public GatewayJwtAuthorizationFilter(UserRepository userRepository, AdminRepository adminRepository) {
        super(Config.class);
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }
    @Override
    public GatewayFilter apply(GatewayJwtAuthorizationFilter.Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("인가 필터 진입!!!");

            //JWT 토큰 검증
//            String header = request.getHeader(JwtProperties.headerString);
            String header = request.getHeaders().getFirst(com.mogumogu.spring.JwtProperties.headerString);
            if (header == null || !header.startsWith(com.mogumogu.spring.JwtProperties.tokenPrefix)) {
                log.info("토큰이 없음!! doFilter 수행");
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            chain.filter(exchange);
            System.out.println("header : " + header);
//            String token = request.getHeader(JwtProperties.headerString)
//                    .replace(JwtProperties.tokenPrefix, "");
            String token = request.getHeaders().getFirst(com.mogumogu.spring.JwtProperties.headerString)
                    .replace(com.mogumogu.spring.JwtProperties.tokenPrefix, "");
            log.info(token);

            String username = JWT.require(Algorithm.HMAC512(JwtProperties.secret)).build().verify(token)
                    .getClaim("username").asString();
            log.info(username);

            if (username != null) {
                PersonEntity person;
                if (username.contains("admin_"))
                    person = adminRepository.findByUsername(username);
                else person = userRepository.findByUsername(username);

                PrincipalDetails principalDetails = new PrincipalDetails(person);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails,
                        null,
                        principalDetails.getAuthorities());

                // 강제로 시큐리티의 세션에 접근하여 값 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            return chain.filter(exchange);
        };

    }
    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpstatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpstatus);
        log.error(error);
        return response.setComplete();
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//    }
}

