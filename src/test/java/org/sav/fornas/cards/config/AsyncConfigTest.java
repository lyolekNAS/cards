package org.sav.fornas.cards.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.assertj.core.api.Assertions.assertThat;

class AsyncConfigTest {

	@Test
	void asyncConfig_shouldHaveConfigurationAndEnableAsyncAnnotations() {
		assertThat(AsyncConfig.class.isAnnotationPresent(Configuration.class)).isTrue();
		assertThat(AsyncConfig.class.isAnnotationPresent(EnableAsync.class)).isTrue();
	}

	@Test
	void asyncConfig_shouldRegisterAsyncBeanPostProcessor() {
		try (var context = new AnnotationConfigApplicationContext(AsyncConfig.class)) {
			assertThat(context.getBeanNamesForType(AsyncAnnotationBeanPostProcessor.class))
					.hasSize(1);
		}
	}

}
