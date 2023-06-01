package com.api.springstore.configs;

import com.api.springstore.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfigurator {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var user = RemoveRole(User.USER);
        var admin = RemoveRole(User.ADMIN);

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET).hasRole(user)
                        .requestMatchers(HttpMethod.PUT).hasRole(admin)
                        .requestMatchers(HttpMethod.POST).hasRole(admin)
                        .requestMatchers(HttpMethod.DELETE).hasRole(admin)
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
        // or PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DefaultWebSecurityExpressionHandler getWebSecurityExpressionHandler() {
        var expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(getRoleHierarchy());
        return expressionHandler;
    }

    @Bean
    public RoleHierarchy getRoleHierarchy() {
        var roleHierarchy = new RoleHierarchyImpl();
        var hierarchy = User.ADMIN + " > " + User.USER;
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    private static String RemoveRole(String auth) {
        return auth.replaceFirst("ROLE_", "");
    }
}
