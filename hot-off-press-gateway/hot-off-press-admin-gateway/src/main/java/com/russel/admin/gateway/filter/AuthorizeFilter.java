package com.russel.admin.gateway.filter;

import com.russel.admin.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Russel
 * @DATE 2023/11/8.
 */
@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1. 获取请求与响应
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //2. 获取请求地址,查看是否包含login路径
        if (request.getURI().getPath().contains("/login")) {
            return chain.filter(exchange);
        }

        //3.获取请求中的token
        String token = request.getHeaders().getFirst("Token");

        try {
            //4.检验Token中的信息
            Claims body = AppJwtUtil.getClaimsBody(token);
            int tokenStatus = AppJwtUtil.verifyToken(body);
            if (tokenStatus > 1) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            Object userId = body.get("id");
            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.set("userId", userId.toString());
            }).build();
            exchange = exchange.mutate().request(serverHttpRequest).build();
        } catch (Exception e) {
            log.error("Token out of date ");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
