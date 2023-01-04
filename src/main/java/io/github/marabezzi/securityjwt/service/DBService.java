package io.github.marabezzi.securityjwt.service;

import org.springframework.stereotype.Service;

import io.github.marabezzi.securityjwt.model.Usuario;
import io.github.marabezzi.securityjwt.model.enums.Perfil;
import io.github.marabezzi.securityjwt.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DBService {
 
	final private UsuarioRepository usuarioRepository;
 
	public void instanciaDB() {
	Usuario u1 = new Usuario(null, "Administrador", "550.482.150-95", "adm@mail.com", "123");
	u1.addPerfil(Perfil.ADMIN);
	
	usuarioRepository.save(u1);
	}
}
