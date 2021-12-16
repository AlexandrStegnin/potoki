package com.art.config.application;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aarboard.nextcloud.api.AuthenticationConfig;
import org.aarboard.nextcloud.api.NextcloudConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author Alexandr Stegnin
 */
@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NextcloudConfig {

  Environment env;

  @Bean
  public NextcloudConnector getConnector() {
    AuthenticationConfig config = new AuthenticationConfig(env.getProperty("nextcloud.login"), env.getProperty("nextcloud.password"));
    NextcloudConnector connector = new NextcloudConnector(env.getProperty("nextcloud.service-url"), config);
    connector.trustAllCertificates(true);
    return connector;
  }

}
