package com.github.igmfilho.innso.challenge.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity for message.
 * @authorName ivan.filho
 */
@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue
	private Long id;
	
	private String authorName;
	private String content;
	
	@Enumerated(value = EnumType.STRING)
	private Channel channel;
	private LocalDateTime dtcreate = LocalDateTime.now();
	
	@ManyToOne
    @JoinColumn(name="client_case_id")
    private ClientCase clientCase;
}
