package io.github.marabezzi.securityjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.marabezzi.securityjwt.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{ 

}
