package com.leonardo.springSecurityCourse.repository;

import com.leonardo.springSecurityCourse.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {

    // usado pela configuracao do jpa que somente passar um item da coluna
    // na tag findBy ele me trara oq ue eu quero
    Users findByUsername(String username);
}
