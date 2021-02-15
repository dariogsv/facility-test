package com.facilit.springapi_restful_facilit.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.facilit.springapi_restful_facilit.model.Produto;
import com.facilit.springapi_restful_facilit.repository.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @GetMapping
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    @PostMapping
    public List<Produto> adicionarProduto(@RequestBody List<Produto> produtos){
        return produtoRepository.saveAll(produtos);
    }
}
