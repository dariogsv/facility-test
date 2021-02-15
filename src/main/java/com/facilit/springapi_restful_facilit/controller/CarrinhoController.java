package com.facilit.springapi_restful_facilit.controller;

import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import com.facilit.springapi_restful_facilit.model.Carrinho;
import com.facilit.springapi_restful_facilit.repository.CarrinhoRepository;
import com.facilit.springapi_restful_facilit.repository.CupomRepository;
import com.facilit.springapi_restful_facilit.repository.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {
    @Autowired
    CarrinhoRepository carrinhoRepository;
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    CupomRepository cupomRepository;
    
    @GetMapping
    public Optional<Carrinho> mostrarCarrinho(@RequestParam long id) {
        return carrinhoRepository.findById(id);
    }

    @PostMapping
    public Carrinho abrirCarrinho(@RequestBody Map<String, String> carrinho){

        return carrinhoRepository.save(new Carrinho(carrinho.get("produtos"), carrinho.get("cupons"), produtoRepository, cupomRepository));
    }

    @PutMapping(value="/adicionar_produtos")
    public Carrinho adicionarProdutos(@RequestParam long id, @RequestBody Map<String, String> carrinho) {

        Optional<Carrinho> car = carrinhoRepository.findById(id);
        System.out.println(carrinho.get("produtos"));
        return car.get().adicionarProdutos(carrinho.get("produtos"), produtoRepository, cupomRepository);
    }

    @PutMapping(value="/adicionar_cupons")
    public Carrinho adicionarCupons(@RequestParam long id, @RequestBody Map<String, String> carrinho) {

        Optional<Carrinho> car = carrinhoRepository.findById(id);
        return car.get().adicionarCupons(carrinho.get("cupons"), produtoRepository, cupomRepository);
    }

    @DeleteMapping(value="/deletar_produtos")
    public Carrinho deletarProdutos(@RequestParam long id, @RequestBody Map<String, String> carrinho) {

        Optional<Carrinho> car = carrinhoRepository.findById(id);
        return car.get().deletarProdutos(carrinho.get("produtos"), produtoRepository, cupomRepository);
    }

    @DeleteMapping(value="/deletar_cupons")
    public Carrinho deletarCupons(@RequestParam long id, @RequestBody Map<String, String> carrinho) {

        Optional<Carrinho> car = carrinhoRepository.findById(id);
        return car.get().deletarCupons(carrinho.get("cupons"), produtoRepository, cupomRepository);
    }
}
