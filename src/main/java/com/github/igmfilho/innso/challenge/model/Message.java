package com.github.igmfilho.innso.challenge.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity for message.
 * @author ivan.filho
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
	
	private String author;
	private String content;
	@Enumerated(value = EnumType.STRING)
	private Channel channel;

	private LocalDateTime dtcreate = LocalDateTime.now();

}
