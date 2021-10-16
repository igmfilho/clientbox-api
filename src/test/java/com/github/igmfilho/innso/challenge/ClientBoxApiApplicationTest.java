package com.github.igmfilho.innso.challenge;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class ClientBoxApiApplicationTest {

	private static final String SPRING_APP_STARTED = "Started ClientBoxApiApplication";

	@Test
	public void shouldStartApplication(CapturedOutput output) {
		ClientBoxApiApplication.main(new String[0]);
		assertThat(output.toString()).contains(SPRING_APP_STARTED);
	}
}
