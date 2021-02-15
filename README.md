# facility-test
Desafio de seleção.

O projeto foi escrito utilizando o Spring Boot com JPA, banco em memória H2 no ambiente do VsCode.

Não consegui realizar a persistencia dos dados, então embora esteja cadastrando produtos, cadastrando carrinhos, 
quando tento alterar dados das entidades, estas não permanecem atualizadas. Vi que tem algo a ver com os estados do JPA,
mas não consegui solucionar o mistério.

Sobre o formato de entrada, o carrinho é identificado por um param id e o restante vai num json no body.

No link abaixo estão algumas das requisições que utilizei.

https://documenter.getpostman.com/view/11576249/TWDTLdfo
