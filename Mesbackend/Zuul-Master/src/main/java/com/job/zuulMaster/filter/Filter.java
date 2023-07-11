package com.job.zuulMaster.filter;

import com.mysql.cj.util.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class Filter implements GlobalFilter , Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path=exchange.getRequest().getURI().getPath();
        if (path.equals("/authen/login")) {
            System.out.println("111");
            return chain.filter(exchange);
        }
//        } else if (path.equals("/authen/hello")) {
//            System.out.println("222");
//            return chain.filter(exchange);
//        }
            else {
            System.out.println("234");
            String token=exchange.getRequest().getHeaders().getFirst("token");
            System.out.println(token);
            if (StringUtils.isNullOrEmpty(token)){
                System.out.println("meiyoutoken");
               return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
