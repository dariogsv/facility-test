package com.facilit.springapi_restful_facilit.controller;

import java.util.List;

import com.facilit.springapi_restful_facilit.model.Cupom;
import com.facilit.springapi_restful_facilit.repository.CupomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cupons")
public class CupomController {
    @Autowired
    private CupomRepository cupomRepository;

    @GetMapping
    public List<Cupom> listar(){
        return cupomRepository.findAll();
    }

    @PostMapping
    public List<Cupom> adicionarCupom(@RequestBody List<Cupom> cupons){
        return cupomRepository.saveAll(cupons);
    }
}
