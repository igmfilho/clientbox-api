package com.github.igmfilho.innso.challenge.api;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.igmfilho.innso.challenge.api.handler.ResourceNotFoundException;
import com.github.igmfilho.innso.challenge.model.ClientCase;
import com.github.igmfilho.innso.challenge.model.Message;
import com.github.igmfilho.innso.challenge.repository.ClientCaseRepository;
import com.github.igmfilho.innso.challenge.repository.MessageRepository;

/**
 * Customizing message resource to support requests for linked relationship with Client Case.
 * 
 * @author ivan.filho
 *
 */
@RestController
@RequestMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
	
	@Autowired
	private ClientCaseRepository clientCaseRepository;

	@Autowired
	private MessageRepository messageRepository;
	
	@PostMapping("/{messageId}/client")
	@Transactional
	public ResponseEntity<?> save(@RequestBody ClientCase clientCase, @PathVariable Long messageId) {
		Message message = messageRepository.findById(messageId).orElseThrow(ResourceNotFoundException::new);

		// Linking Client Case with the message
		clientCase.setMessages(List.of(message));
		ClientCase saved = clientCaseRepository.save(clientCase);
		
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{messageId}")
    public ResponseEntity<?> getById(@PathVariable Long messageId) {
		Message message = messageRepository.findById(messageId).orElseThrow(ResourceNotFoundException::new);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
