package com.crud.comunidades.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.crud.comunidades.model.Comunidade;
import com.crud.comunidades.repository.ComunidadeRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comunidade")
public class ComunidadeController {

	@Autowired
	ComunidadeRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Comunidade>> getComunidade() {
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Comunidade>>getComunidadeById(@PathVariable int id) {
		Optional<Comunidade> comunidade = repository.findById(id);
		
		if (comunidade.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {			
			return ResponseEntity.ok(comunidade);
		}
		
	}
	
	@PostMapping
	public ResponseEntity<Comunidade> insert(@RequestBody @Valid Comunidade comunidade) {
		repository.save(comunidade);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comunidade.getId()).toUri()).build();
	}
	
	@PutMapping("/{id}") 
	public ResponseEntity<Comunidade> update(@PathVariable int id, @RequestBody @Valid Comunidade comunidade){
		
		Optional<Comunidade> comunidadeExistente = repository.findById(id);
		if(comunidadeExistente.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Comunidade comunidadeAtualizada = comunidadeExistente.get();
		comunidadeAtualizada.setNome(comunidade.getNome());
	
		try {
			repository.save(comunidadeAtualizada);
			return ResponseEntity.ok(comunidadeAtualizada);
		} catch (Exception error) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		
		Optional<Comunidade> comunidadeExistente = repository.findById(id);
		if(comunidadeExistente.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
