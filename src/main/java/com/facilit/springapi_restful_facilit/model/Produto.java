package com.facilit.springapi_restful_facilit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private float valor;
    private String tipo;
    private boolean desconto10OuMais = false;
    
    @Transient
    private int quantidade = 1;

    public void incQuantidade(){
        quantidade += 1;
    }
}
