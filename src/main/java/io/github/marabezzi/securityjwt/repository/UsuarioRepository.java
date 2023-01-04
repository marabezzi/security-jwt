package io.github.marabezzi.securityjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.marabezzi.securityjwt.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{ 
	
	@Query("select u from Usuario u where u.email like :email AND u.ativo = true")
	Optional<Usuario> findByEmailAndAtivo(String email);

}
