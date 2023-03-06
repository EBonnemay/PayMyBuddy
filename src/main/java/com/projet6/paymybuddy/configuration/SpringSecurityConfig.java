package com.projet6.paymybuddy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
public class SpringSecurityConfig  {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {//cette méthode gère les requêtes http
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                        "/resources/**",
                        "/css/**",
                        "/fonts/**",
                        "/img/**").permitAll()
                        .requestMatchers(request -> request.getRequestURI().equals("/registerPage")).permitAll() // allow homePage without authentication
                        //.requestMatchers(RequestMethod.GET, "/registerPage").permitAll() // allow registerPage without authentication
                        //.requestMatchers(RequestMethod.POST, "/registerNewUser").permitAll() // allow POST to registration without authentication
                        .requestMatchers(request -> request.getRequestURI().equals("/registerNewUser")).permitAll()
                        .requestMatchers(request -> request.getRequestURI().equals("/logout")).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin()//formulaire spring security par défaut
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/personalPage")
                    //.failureUrl("/login?error=true")
                    .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutUrl("/logout")
                .logoutSuccessUrl("/login");

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }




    @Bean
    public PasswordEncoder getPasswordEncoder() {//encode les mots de passe > algorithme de hachage
        // Si les mots de passe sont en clair en base de données
        // Uniquement pour la phase de développement
        //return NoOpPasswordEncoder.getInstance();
        // Si les mots de passe sont chiffrés avec BCrypt
        return new BCryptPasswordEncoder();
    }
   //@Bean
    //public HttpSecurity httpSecurity() throws Exception{
    //return super.configure(new HttpSecurity());

   // }
    @Bean
    public AuthenticationManager authenticationManager(//crée une chaine de sésurité
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Autowired
    private UserDetailsService userDetailService;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider  = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(getPasswordEncoder());
        System.out.println("security provider is about to return");
        return provider;
    }
}

