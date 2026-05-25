package org.sav.fornas.cards.property;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class GooglePropertiesTest {

	@Test
	void gettersAndSettersShouldWork() {
		GoogleProperties properties = new GoogleProperties();

		properties.setApiKey("test-api-key");
		properties.setTranslateUrl("https://translation.googleapis.com/language/translate");

		assertThat(properties.getApiKey()).isEqualTo("test-api-key");
		assertThat(properties.getTranslateUrl()).isEqualTo("https://translation.googleapis.com/language/translate");
	}

	@Test
	void shouldBindFromConfigurationPrefix() {
		MockEnvironment environment = new MockEnvironment()
				.withProperty("app-props.google.api-key", "bound-key")
				.withProperty("app-props.google.translate-url", "https://example.org/translate");

		GoogleProperties properties = Binder.get(environment)
				.bind("app-props.google", Bindable.of(GoogleProperties.class))
				.orElseThrow(() -> new IllegalStateException("Properties were not bound"));

		assertThat(properties.getApiKey()).isEqualTo("bound-key");
		assertThat(properties.getTranslateUrl()).isEqualTo("https://example.org/translate");
	}
}
