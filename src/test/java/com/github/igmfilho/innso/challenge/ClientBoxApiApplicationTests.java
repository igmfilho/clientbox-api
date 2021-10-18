package com.github.igmfilho.innso.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import com.jayway.jsonpath.JsonPath;

import lombok.extern.java.Log;

@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Log
class ClientBoxApiApplicationTests {

	private static final String SPRING_APP_STARTED = "Started ClientBoxApiApplication";

	private static final String CLIENT_CASES_RESOURCE = "/clientCases";
	private static final String MESSAGES_RESOURCE = "/messages";
	private static final String CLIENT_CASES_FIND_BY_REF_RESOURCE = "/search/findByReference";

	@Autowired
	private MockMvc mockMvc;

	private static ObjectMapper objectMapper;
	
	/**
	 * Setting up properties for the tests.
	 * @author ivan.filho
	 */
	@BeforeAll
    public static void setup() {
		objectMapper = new ObjectMapper().disable(MapperFeature.USE_ANNOTATIONS);
		// Define dates as ISO
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		
		log.info("All settings have been completed");
    }
	
	@Test
	@Order(1) 
	public void whenAppIsStarted_thenOutputShouldContainsAString(CapturedOutput output) {
		ClientBoxApiApplication.main(new String[0]);
		assertThat(output.toString()).contains(SPRING_APP_STARTED);
	}

	/**
	 * Scenario 01:
	 *  
	 * Create a message from « Jérémie Durand » with the following content:
	 * « Hello, I have an issue with my new phone »
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(2)
	public void givingMessageMail_thenResourceShouldBeCreated() throws Exception {
		Message message = new Message();
		message.setAuthorName("Jérémie Durand");
		message.setContent("Hello, I have an issue with my new phone");
		message.setChannel(Channel.MAIL);
		
		mockMvc.perform(post(MESSAGES_RESOURCE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(message)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("/messages")));
	}

	/**
	 * Scenario 02:
	 *  
	 * Create a message from « Jérémie Durand » with the following content:
	 * « Hello, I have an issue with my new phone »
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(3)
	public void givingClientCase_thenResourceShouldBeCreatedAndLinkedwithPreviousMessage(CapturedOutput output) throws Exception {
		
		ResultActions resultMessage = mockMvc.perform(get(MESSAGES_RESOURCE))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.messages.length()").value(1));

		String clientCaseRelLinkFromMessageResource = JsonPath.read(resultMessage.andReturn().getResponse().getContentAsString(), "$._embedded.messages[0]._links.clientCase.href");
		
		ClientCase clientCase = new ClientCase();
		clientCase.setClientName("Jérémie Durand");

		mockMvc.perform(post(clientCaseRelLinkFromMessageResource.replace("clientCase","client"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientCase))
				)
		.andDo(print())
		.andExpect(status().isCreated())
		;
	}
	
	/**
	 * Scenario 03:
	 *  
	 * Creation of a message from « Sonia Valentin », with the following content: 
	 * « I am Sonia, and I will do my best to help you. What is your phone brand and model? »
	 *   This message will be linked the previously created client case.
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(4) 
	public void givingMessage_thenResourceShouldBeCreatedAndLinkedwithPreviousClient() throws Exception {
		
		Message message = new Message();
		message.setAuthorName("Sonia Valentin");
		message.setContent("I am Sonia, and I will do my best to help you. What is your phone brand and model?");
		message.setChannel(Channel.TWITTER);

		ResultActions resultMessage = mockMvc.perform(post(MESSAGES_RESOURCE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(message))
				)
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", containsString("/messages")));
		
		String messageLocation = resultMessage.andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
		
		ResultActions resultClientCase = mockMvc.perform(get(CLIENT_CASES_RESOURCE))
		.andDo(print())
		.andExpect(status().isOk())
		;
		
		String clientCaseLocation = JsonPath.read(resultClientCase.andReturn().getResponse().getContentAsString(), "$._embedded.clientCases[0]._links.self.href");
		
		mockMvc.perform(put(messageLocation + "/clientCase")
				.contentType("text/uri-list")
				.content(clientCaseLocation)
				)
		.andDo(print())
		.andExpect(status().isNoContent())
		;
		
		mockMvc.perform(get(clientCaseLocation + MESSAGES_RESOURCE)
				)
		.andDo(print())
		.andExpect(status().isOk())
		;
	}
	
	
	/**
	 * Scenario 04:
	 *  
	 * Modification of the client case adding the client reference « KA-18B6 ».
	 * This will validate the client case modification feature.
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(5) 
	public void givingClientReferente_thenResourceShouldBeModified() throws Exception {
		
		ResultActions resultClientCase = mockMvc.perform(get(CLIENT_CASES_RESOURCE))
		.andDo(print())
		.andExpect(status().isOk())
		;
		
		String clientCaseLocation = JsonPath.read(resultClientCase.andReturn().getResponse().getContentAsString(), "$._embedded.clientCases[0]._links.self.href");
	
		String reference = "KA-18B6";

		ClientCase clientCase = new ClientCase();
		clientCase.setReference(reference);
		
		mockMvc.perform(patch(clientCaseLocation)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientCase))
				)
		.andDo(print())
		.andExpect(status().isNoContent())
		;
		
		
		mockMvc.perform(get(CLIENT_CASES_RESOURCE))
		.andDo(print())
		.andExpect(status().isOk())
		;
		
		mockMvc.perform(get(CLIENT_CASES_RESOURCE + CLIENT_CASES_FIND_BY_REF_RESOURCE + "?reference=" + reference))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$._embedded.clientCases[0].reference", is(reference)))
		.andExpect(jsonPath("$._embedded.clientCases[0].clientName").value("Jérémie Durand"))
		;
	}
	
	/**
	 * Scenario 05:
	 *  
	 * Fetching of all client cases.
	 * The result will only contains one client case, the one we created before.
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(6) 
	public void whenRequestClientResources_thenResultShouldContainAllClients() throws Exception {
		
		ResultActions resultClientCase = mockMvc.perform(get(CLIENT_CASES_RESOURCE))
				.andDo(print())
				.andExpect(status().isOk())
				;
				
		String clientCaseLocation = JsonPath.read(resultClientCase.andReturn().getResponse().getContentAsString(), "$._embedded.clientCases[0]._links.self.href");
				
		mockMvc.perform(get(clientCaseLocation + MESSAGES_RESOURCE))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.messages.length()").value(1))
				;
	}
}
