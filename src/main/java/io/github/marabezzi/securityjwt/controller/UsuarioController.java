package io.github.marabezzi.securityjwt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.marabezzi.securityjwt.model.Usuario;
import io.github.marabezzi.securityjwt.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/usuarios")
@AllArgsConstructor
public class UsuarioController {
	
	private final UsuarioRepository usuarioRepository;
	
	@GetMapping
	public List<Usuario> list() {
		return usuarioRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> findById(@PathVariable Long id) {
		return usuarioRepository.findById(id)
				.map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Usuario create(@RequestBody Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	/* @PutMapping("/{id}")
	public ResponseEntity<Usuario> update(@PathVariable Long id,
										 @RequestBody Usuario usuario) {
		return usuarioRepository.findById(id)
			.map(recordFound -> {
				recordFound.setName(course.getName());
				recordFound.setCategory(course.getCategory());
				Course updated = courseRepository.save(recordFound);
				return ResponseEntity.ok().body(updated);
			})
		.orElse(ResponseEntity.notFound().build());≈
	} */
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return usuarioRepository.findById(id)
				.map(recordFound -> {
					usuarioRepository.deleteById(id);
					return ResponseEntity.noContent().build();
				})
			.orElse(ResponseEntity.notFound().build());
		}

}
