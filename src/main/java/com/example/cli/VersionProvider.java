package com.example.cli;

import java.io.IOException;
import java.util.Properties;
import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

  @Override
  public String[] getVersion() throws IOException {
    var props = new Properties();
    try (var stream = getClass().getResourceAsStream("/version.properties")) {
      if (stream == null) {
        return new String[] {"unknown"};
      }
      props.load(stream);
    }
    return new String[] {
      String.format(
          "%s %s (built %s)",
          props.getProperty("name", "unknown"),
          props.getProperty("version", "unknown"),
          props.getProperty("build.timestamp", "unknown")),
    };
  }
}
