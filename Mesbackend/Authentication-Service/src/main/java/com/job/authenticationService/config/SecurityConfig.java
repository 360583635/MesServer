package com.job.authenticationService.config;

import com.job.authenticationService.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeHttpRequests((authz) -> authz
//                                .requestMatchers("/hello").permitAll()
//                        .requestMatchers("/authen/login").anonymous()
////                        .anyRequest().authenticated()
//                );
        http.csrf().disable();

        http.authorizeHttpRequests((authz)->authz
                .requestMatchers("/authen/login").anonymous()
                .requestMatchers("/authen/**").permitAll()
                .requestMatchers("/hello").permitAll()
                .requestMatchers("/getRoles").permitAll()
                .requestMatchers("/delRole/*").permitAll()
                .requestMatchers("/delUser/*").permitAll()
                .requestMatchers("/addRole").permitAll()
                .requestMatchers("/updateRole").permitAll()
                .requestMatchers("/addUser").permitAll()
                .requestMatchers("/updateUser").permitAll()
                .requestMatchers("/update/detail").permitAll()
                .requestMatchers("/authen/menus/**").permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors();

        return http.build();
    }
    //    @Configuration
//    public class SecurityConfiguration {
//        @Bean
//        public WebSecurityCustomizer webSecurityCustomizer() {
//            return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");
//        }
//    }
//@Bean
//public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//    return authenticationConfiguration.getAuthenticationManager();
//}
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
