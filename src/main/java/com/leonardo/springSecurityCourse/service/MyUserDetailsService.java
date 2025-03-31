package com.leonardo.springSecurityCourse.service;

import com.leonardo.springSecurityCourse.model.Users;
import com.leonardo.springSecurityCourse.model.UserPrincipal;
import com.leonardo.springSecurityCourse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    // eu subescrevo ele aqui pois ele e setado para ser utilizado no securityconfig
    // ou seja ele precisa de novas implementacoes, nisso eu chamo este metodo pela interface userdetailsservice
    // ou seja assim ele fica mais facil de setar a opcoes que eu quero
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repository.findByUsername(username);

        if(user == null) {
            System.out.println("User not found.");
            throw new UsernameNotFoundException("User not found.");
        }

        return new UserPrincipal(user);
    }
}
