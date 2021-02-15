package com.facilit.springapi_restful_facilit.repository;


import com.facilit.springapi_restful_facilit.model.Cupom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {

	
    
}
