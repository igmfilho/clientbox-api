package com.github.igmfilho.innso.challenge.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity for Client Case.
 * @author ivan.filho
 */
@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ClientCase {

	@Id
	@GeneratedValue
	private Long id;
	
	private String customerName;
	private String reference;

	private LocalDateTime dtcreate = LocalDateTime.now();
	
	@OneToMany
	private List<Message> messages;
}

