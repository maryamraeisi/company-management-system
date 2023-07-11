package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .antMatchers("/", "/api/v*/company_management_system/").permitAll()
                .antMatchers("**/employee_profile/**").hasRole("MANAGER")//TODO: it's not working :(
//                .antMatchers("employee_profile").hasRole("EMPLOYEE")
//                .antMatchers("employee_profile")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        List<AuthenticationProvider> providers =  List.of(authProvider);

        return new ProviderManager(providers);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Bean
//    @Qualifier("userDetailsService")
//    public UserDetailsService userDetailsService() {
//        UserDetails maryam = User.builder()
//                .username("maryam")
//                .password(passwordEncoder().encode("password"))
//                .roles("MANAGER") //ROLE_MANAGER
//                .build();
//        UserDetails niloofar = User.builder()
//                .username("niloofar")
//                .password(passwordEncoder().encode("password"))
//                .roles("EMPLOYEE") //ROLE_EMPLOYEE
//                .build();
//        return new InMemoryUserDetailsManager(maryam, niloofar);
//    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("Maryam123456789");
        dataSource.setUrl("jdbc:mysql://localhost/company_management_system");
        return dataSource;
    }

//    @Bean
//    @Qualifier("userDetailsManager")
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("password"))
//                .roles("EMPLOYEE")
//                .build();
//        UserDetails maryam = User.builder()
//                .username("maryam")
//                .password(passwordEncoder().encode("password"))
//                .roles("MANAGER")
//                .build();
//        UserDetails niloofar = User.builder()
//                .username("niloofar")
//                .password(passwordEncoder().encode("password"))
//                .roles("EMPLOYEE")
//                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        return users;
//    }

}
