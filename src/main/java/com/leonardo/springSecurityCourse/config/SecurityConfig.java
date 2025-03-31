package com.leonardo.springSecurityCourse.config;

import com.leonardo.springSecurityCourse.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// cria-se a classe e coloca ela como configuration para definir certas coisas
// no spring securtity para um melhor comportamento e editar conforma a necessidade
// da aplicacao a ser criada
@Configuration
@EnableWebSecurity // aqui ele tira a seguranca que esta colocada por default
public class SecurityConfig {


        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {




        // nestas configuracoes o professor utiliza lambda uma funcionalidade que faz
        // o desenvolvedor nao precsar ficar substituindo toda hora a interface chamada
        // pq na maioria desta configuracoes ele esta mudando os metodos da respectiva interface
        // aqui ele esta disabilitando o csrf
        http.csrf(customizer -> customizer.disable());
        // aqui ele esta falando para somente entrar quem esta authenticado
        // com o requestMatchters eu posso escoloher qual eu quero que ele se comporte de um jeito
        // neste caso e para ter a necessidade do usuario colocar o login ou nao
        http.authorizeHttpRequests(request -> request
                .requestMatchers("register", "login")
                .permitAll().anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults());
        // aqui ele esta disponibilizando o formulario para o usuario logar
        //http.formLogin(Customizer.withDefaults());
        // para ser disponibilizado no postman ou insommia
        http.httpBasic(Customizer.withDefaults());
        // aqui ele cria toda vez um novo id para o usuario e melhor para a seguranca
        // dependendo da aplicacao que sera realizada
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // aqui e como ele lida com a senha que vem do banco de dados, esta opcao que esta
        // indisponivel abaixo e a defaul
        // provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        // aqui eu coloco como eu quero desriptografar a senha do banco de dados
        // lembrando que tem que ser como eu criptografei para o banco de dados
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


    // lidando com o token, fazendo a sua respectiova configuracao
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }





    /*
    // usado para cadastrar um novo usuario na parte de desenvolvimento
    // de forma a ter mais desenvolvedores na aplicacao
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User
                .withDefaultPasswordEncoder()
                .username("leo")
                .password("123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1);
    }
    */




}
