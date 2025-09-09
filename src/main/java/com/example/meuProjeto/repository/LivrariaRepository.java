package com.example.meuProjeto.repository;

import com.example.meuProjeto.model.Livraria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivrariaRepository extends JpaRepository<Livraria, String> {
    List<Livraria> findByTitulo(String titulo);
}
