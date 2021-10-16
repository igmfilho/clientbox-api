package com.github.igmfilho.innso.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.igmfilho.innso.challenge.model.Message;

/**
 * Message repository to support CRUD operation data.
 * @author ivan.filho
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

}
