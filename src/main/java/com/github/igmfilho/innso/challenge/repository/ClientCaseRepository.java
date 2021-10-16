package com.github.igmfilho.innso.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.igmfilho.innso.challenge.model.ClientCase;

/**
 * Client Case repository to support CRUD operation data.
 * @author ivan.filho
 */
public interface ClientCaseRepository extends JpaRepository<ClientCase, Long> {

}
