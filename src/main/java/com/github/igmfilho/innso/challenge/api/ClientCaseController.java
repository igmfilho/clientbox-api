package com.github.igmfilho.innso.challenge.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.igmfilho.innso.challenge.api.handler.ResourceNotFoundException;
import com.github.igmfilho.innso.challenge.model.ClientCase;
import com.github.igmfilho.innso.challenge.repository.ClientCaseRepository;

/**
 * Customizing Client Case resource to handle behavior of operations 
 * 
 * @author ivan.filho
 *
 */
@RestController
@RequestMapping(value = "/clientCases", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientCaseController {

	@Autowired
	private ClientCaseRepository clientCaseRepository;

	@GetMapping("/{clientCaseId}")
    public ResponseEntity<?> getById(@PathVariable Long clientCaseId) {
		ClientCase message = clientCaseRepository.findById(clientCaseId).orElseThrow(ResourceNotFoundException::new);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}