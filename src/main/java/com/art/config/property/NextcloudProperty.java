package com.art.config.property;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Alexandr Stegnin
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NextcloudProperty {

  Environment environment;

  public String getRemoteFolder() {
    return File.separator + environment.getProperty("nextcloud.folder") + File.separator;
  }

}
