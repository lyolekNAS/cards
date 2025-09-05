package org.sav.fornas.cards.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app-props.google")
@Getter
@Setter
public class GoogleProperties {
	private String apiKey;
	private String translateUrl;
}
