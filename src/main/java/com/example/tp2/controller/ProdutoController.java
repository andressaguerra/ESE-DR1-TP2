package com.example.tp2.controller;

import com.example.tp2.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
        produtoRepository.saveAll(List.of(
                new Produto("Pão francês", 1.69),
                new Produto("Pão de queijo", 4.3),
                new Produto("Croissant", 14.99),
                new Produto("Bolo de laranja", 19.99),
                new Produto("Pudim de leite", 13.99),
                new Produto("Suco de laranja", 14.99),
                new Produto("Mate da casa", 11.99)
        ));
    }

    @GetMapping("/produtos")
    Iterable<Produto> getProdutos() {
        return produtoRepository.findAll();
    }

    @GetMapping("/produtos/{id}")
    Optional<Produto> getProdutosPorId(@PathVariable int id) {
        return produtoRepository.findById(id);
    }

    @DeleteMapping("/produtos/{id}")
    ResponseEntity<Produto> deleteProduto(@PathVariable int id) {
        if(produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/produtos")
    ResponseEntity<Produto> addProduto(@RequestBody Produto produto) {
        if(!produto.getNome().isEmpty()) {
            produtoRepository.save(produto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/produtos/{id}")
    ResponseEntity<Produto> updateProduto(@PathVariable int id, @RequestBody Produto produto) {
        return produtoRepository.findById(id)
                .map(existingProduto -> {
                    produto.setId(existingProduto.getId());
                    return ResponseEntity.ok(produtoRepository.save(produto));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto)));
    }
}