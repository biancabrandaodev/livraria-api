package com.example.meuProjeto.controller;

import com.example.meuProjeto.model.Livraria;
import com.example.meuProjeto.repository.LivrariaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
public class LivrariaController {

    private LivrariaRepository livrariaRepository;
    public LivrariaController(LivrariaRepository livrariaRepository) {
        this.livrariaRepository = livrariaRepository;
    }

    @GetMapping("/{id}")
    public Livraria obterPorId(@PathVariable("id") String id) {
        return livrariaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public void salvar(@RequestBody Livraria livraria) {

        String id = UUID.randomUUID().toString();
        livraria.setId(id);
        livrariaRepository.save(livraria);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable("id") String id) {
        livrariaRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void atualizar(@PathVariable("id") String id, @RequestBody Livraria livraria) {
        livraria.setId(id);
        livrariaRepository.save(livraria);
    }

    @GetMapping
    public List<Livraria> buscar(@RequestParam("titulo") String titulo) {
        return livrariaRepository.findByTitulo(titulo);
    }

}
