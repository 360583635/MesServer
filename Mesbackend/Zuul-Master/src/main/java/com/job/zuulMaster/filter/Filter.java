package com.job.zuulMaster.filter;

//import com.job.authenticationService.pojo.LoginUser;
//import com.job.authenticationService.utils.JwtUtil;

//import com.job.zuulMaster.redis.RedisCache;
import com.job.zuulMaster.utils.Query;
import com.job.zuulMaster.mapper.MenusMapper;
import com.job.zuulMaster.utils.JwtUtil;
import com.mysql.cj.util.StringUtils;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class Filter implements GlobalFilter , Ordered {
    @Autowired
    private Query querys;

    @Autowired
    private MenusMapper menusMapper;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path=exchange.getRequest().getURI().getPath();
//        System.out.println(path);
//        ThreadLocal threadLocal = new InheritableThreadLocal();
//        threadLocal.set("userid");
//        if(exchange.getRequest().getURI().toString().contains("order")) return chain.filter(exchange);
        String token=exchange.getRequest().getHeaders().getFirst("token");

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
            System.out.println(token);
            if (StringUtils.isNullOrEmpty(token)){
                System.out.println("没有token就跳转到登录界面");
                String redirectUrl = "http://localhost:8080/#/pages/pt/login/login";
                exchange.getResponse().getHeaders().set(HttpHeaders.LOCATION, redirectUrl);
                //303状态码表示由于请求对应的资源存在着另一个URI，应使用GET方法定向获取请求的资源
                exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(HttpStatus.SC_SEE_OTHER));
                exchange.getResponse().getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
                return exchange.getResponse().setComplete();

            }
            else {
                //解析token
                String userid;
                try {
                    Claims claims = JwtUtil.parseJWT(token);
                    userid = claims.getSubject();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("token非法");
                }
                System.out.println(userid);
                //更具id查找到Usres,然后看Usesde id 是否在Blacklist中，在的化直接返回。
                List<String > blackList=querys.query();
                System.out.println(blackList);
                if (blackList.contains(userid)){
                    System.out.println("不给访问");
                    return exchange.getResponse().setComplete();
                }else {

                    List<String > urllist= menusMapper.selectPermsByUserId(userid);
                    for (String s : urllist) {
                        if (path.equals(s)){
                            System.out.println("hjsgfh");
                            return chain.filter(exchange);
                        }
                    }
                    return exchange.getResponse().setComplete();
                }

            }

        }
    }
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 错误信息响应到客户端
     * @param serverHttpResponse Response
     * @param resultCode 错误枚举
     * @date: 2021/4/20 9:13
     * @return: reactor.core.publisher.Mono<java.lang.Void>
     */


}
