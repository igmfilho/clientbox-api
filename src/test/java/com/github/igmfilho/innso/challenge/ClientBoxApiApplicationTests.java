package com.github.igmfilho.innso.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.igmfilho.innso.challenge.model.Channel;
import com.github.igmfilho.innso.challenge.model.ClientCase;
import com.github.igmfilho.innso.challenge.model.Message;

import lombok.extern.java.Log;

@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(SpringExtension.class)

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log
class ClientBoxApiApplicationTests {

	private static final String SPRING_APP_STARTED = "Started ClientBoxApiApplication";

	private static final String CLIENT_ENDPOINT = "/clientCases";
	private static final String MESSAGES_ENDPOINT = "/messages";

	@Test
	public void shouldStartApplication(CapturedOutput output) {
		ClientBoxApiApplication.main(new String[0]);
		assertThat(output.toString()).contains(SPRING_APP_STARTED);
	}
	
	
	
	@Autowired
	private MockMvc mockMvc;

	private static ObjectMapper objectMapper;
	
	/**
	 * Before all: initial configuration of the tests. Done once only.
	 *
	 * @author iva.filho
	 */
	@BeforeAll
    public static void setup() {
		objectMapper = new ObjectMapper().disable(MapperFeature.USE_ANNOTATIONS);
		// Manage dates as ISO strings
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// Do not put null fields in serialization
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		
		log.info("Setup completed");
    }
		
	@Test
	public void validateFullScenario_thenStatusIsOk() throws Exception {
		
		Message message = new Message();
		message.setAuthorName("Jérémie Durand");
		message.setContent("Bonjour, j’ai un problème avec mon nouveau téléphone");
		message.setChannel(Channel.TWITTER);

		ResultActions resultMessage = mockMvc.perform(post(MESSAGES_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(message))
				)
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", containsString("/messages")));
		
		String messageLocation = resultMessage.andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
		
		ClientCase clientCase = new ClientCase();
		clientCase.setClientName("Jérémie Durand");		

		ResultActions resultCustomerFile = mockMvc.perform(post(CLIENT_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientCase))
				)
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(header().string(HttpHeaders.LOCATION, containsString(CLIENT_ENDPOINT)))
		;
		
		String clientCaseLocation = resultCustomerFile.andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
		
		mockMvc.perform(put(messageLocation + "/clientCase")
				.contentType("text/uri-list")
				.content(clientCaseLocation)
				)
		.andDo(print())
		.andExpect(status().isNoContent())
		;
		
		mockMvc.perform(get(clientCaseLocation + MESSAGES_ENDPOINT)
			
				)
		.andDo(print())
		.andExpect(status().isOk())
		;

	}

}
