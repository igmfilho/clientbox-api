package com.github.igmfilho.innso.challenge.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.igmfilho.innso.challenge.model.ClientCase;

/**
 * Client Case repository to support CRUD operation data.
 * @author ivan.filho
 */
public interface ClientCaseRepository extends CrudRepository<ClientCase, Long> {
	
	List<ClientCase> findByReference(String reference);

}
