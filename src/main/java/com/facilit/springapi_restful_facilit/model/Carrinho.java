package com.facilit.springapi_restful_facilit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.facilit.springapi_restful_facilit.repository.CupomRepository;
import com.facilit.springapi_restful_facilit.repository.ProdutoRepository;

import org.apache.catalina.Manager;

import lombok.Data;

@Data
@Entity
public class Carrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Transient
    private List<Produto> listProdutos = new ArrayList<Produto>();
    @Transient
    private List<Cupom> listCupons = new ArrayList<Cupom>();

    private String produtos;
    private String cupons;

    private double valorTotal;
    private double valorParcial;


    public Carrinho(String produtos, String cupons, ProdutoRepository produtoRepository, CupomRepository cupomRepository){

        this.produtos = produtos;
        this.cupons = cupons;

        listProdutos = stringToListProdutos(produtoRepository);
        listCupons = stringToListCupons(cupomRepository);

        this.valorParcial = valorParcial();
        this.valorTotal = valorFinal();
    }

    public Carrinho(){

    }

    public List<Produto> stringToListProdutos(ProdutoRepository produtoRepository){
        String[] splitProdutos = produtos.split(";");
        
        for(String id : splitProdutos){
            // System.out.println(produtoRepository.findAll());
            Optional<Produto> produto = produtoRepository.findById(Long.parseLong(id));
            if(produto.isPresent()) {
                if(listProdutos.contains(produto.get()))
                    listProdutos.get(listProdutos.indexOf(produto.get())).incQuantidade();
                else 
                    listProdutos.add(produto.get());
            }
        }
        return listProdutos;
    }

    public List<Cupom> stringToListCupons(CupomRepository cupomRepository) {
        String[] splitCupons = cupons.split(";");

        for (String id : splitCupons) {
            Optional<Cupom> cupom = cupomRepository.findById(Long.parseLong(id));
            if(cupom.isPresent()) {
                if(listCupons.contains(cupom.get()))
                    listCupons.get(listCupons.indexOf(cupom.get())).setQuantidade(+1);
                listCupons.add(cupom.get());
            }
        }
        return listCupons;
    }

    public double valorParcial(){
        double total = 0;
        for(Produto produto : listProdutos){            // Soma com os descontos por produto aplicados 
            total += produto.getValor()*produto.getQuantidade();
        }
        return total;
    }

    public void setaProdutosComDesconto10OuMais(){
        Map<String, Integer> tipos = new HashMap<String, Integer>();
        for(Produto produto : listProdutos){            // Soma os tipos de produtos diferentes
            String tipo;
            tipo = produto.getTipo();
            
            if(tipos.containsKey(tipo))
                tipos.replace(tipo, tipos.get(tipo) + 1);
            else{
                tipos.put(tipo, 1);
            }
        }

        for(Produto produto : listProdutos){            // Define o desconto10OuMais em cada produto da lista que tiver o desconto
            String tipo;
            tipo = produto.getTipo();

            if(tipos.containsKey(tipo))
                if(tipos.get(tipo) >= 10)
                    produto.setDesconto10OuMais(true);
        }
    }

    public double valorComDesconto10OuMais(){
        double total = 0;
        for(Produto produto : listProdutos){            // Soma com os descontos por produto aplicados 
            if(produto.isDesconto10OuMais())
                total += produto.getValor()*0.9*produto.getQuantidade();
            else
                total += produto.getValor()*produto.getQuantidade();
        }
        return total;
    }

    public double valorComDescontoSobreGasto(){
        double total = valorComDesconto10OuMais();
        double descontoSobreGasto = 1;
        
        if(total > 10000)                           // Define desconto global sobre o total de acordo com seu patamar de gasto
            descontoSobreGasto = 0.9;
        else if(total > 5000)
            descontoSobreGasto = 0.93;
        else if(total > 1000)
            descontoSobreGasto = 0.95;
        return total*descontoSobreGasto;
    }

    public double melhorDescontoCupom(){
        Cupom melhorCupom = new Cupom();
        double total = valorComDesconto10OuMais();
        double melhorDesconto = 0, descontoAtual = 0;
        
        for(Cupom cupom : listCupons){
            melhorDesconto = total*melhorCupom.getPercentual()+melhorCupom.getLiteral();
            descontoAtual = total*cupom.getPercentual()+cupom.getLiteral();

            if(descontoAtual > melhorDesconto){
                melhorCupom = cupom;
            }
        }

        return melhorDesconto;
    }
    
    public double valorFinal(){

        if(listProdutos == null)
            return 0;

        setaProdutosComDesconto10OuMais();

        if(listCupons.size() <= 0)
            return valorComDescontoSobreGasto();
        
        return valorComDesconto10OuMais()-melhorDescontoCupom();
    }

    public Carrinho adicionarProdutos(String produtosId, ProdutoRepository produtoRepository, CupomRepository cupomRepository){
        produtos = produtos.concat(";").concat(produtosId);
        stringToListProdutos(produtoRepository);
        stringToListCupons(cupomRepository);
        valorParcial = valorParcial();
        valorTotal = valorFinal();
        return this;
    }

    public Carrinho adicionarCupons(String cuponsId, ProdutoRepository produtoRepository, CupomRepository cupomRepository) {
        cupons = cupons.concat(";").concat(cuponsId);
        stringToListProdutos(produtoRepository);
        stringToListCupons(cupomRepository);
        valorParcial = valorParcial();
        valorTotal = valorFinal();
        return this;
    }

    public Carrinho deletarProdutos(String produtosId, ProdutoRepository produtoRepository, CupomRepository cupomRepository) {
        String[] produtosIdSplitted = produtosId.split(";");
        for(String produtoId : produtosIdSplitted){
            if(produtos.contains(produtoId))
                produtos.replaceFirst(produtoId, "");
        }
        stringToListProdutos(produtoRepository);
        stringToListCupons(cupomRepository);
        valorParcial = valorParcial();
        valorTotal = valorFinal();
        return this;
    }

    public Carrinho deletarCupons(String cuponsId, ProdutoRepository produtoRepository, CupomRepository cupomRepository) {
        String[] cuponsIdSplitted = cuponsId.split(";");
        for(String cupomId : cuponsIdSplitted){
            if(cupons.contains(cupomId))
                cupons.replaceFirst(cupomId, "");
        }
        stringToListProdutos(produtoRepository);
        stringToListCupons(cupomRepository);
        valorParcial = valorParcial();
        valorTotal = valorFinal();
        return this;
    }
}
