package ch.verno.ui.client;

import org.springframework.web.client.RestClient;

public abstract class BaseApiClient {

  protected final RestClient restClient;

  protected BaseApiClient(final RestClient restClient) {
    this.restClient = restClient;
  }

  protected static RestClient build(final String baseUrl) {
    return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
  }
}