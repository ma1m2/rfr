package io.student.rangiffler.config;

enum LocalConfig implements Config {
  INSTANCE;

  @Override
  public String frontUrl() {
    return System.getProperty("rfr.front.url", "http://localhost:3001/");
  }

  @Override
  public String authUrl() {
    return System.getProperty("rfr.auth.url", "http://localhost:9001/");
  }
}
