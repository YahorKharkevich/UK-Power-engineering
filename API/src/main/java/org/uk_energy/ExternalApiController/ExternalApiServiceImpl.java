package org.uk_energy.ExternalApiController;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties(prefix = "external.api")
public class ExternalApiServiceImpl implements ExternalApiService {
    private String baseUrl;
    private int timeoutMs;
    private final RestTemplate restTemplate;

    public ExternalApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public ExternalApiData getData(String from, String to) {
        ExternalApiData data = restTemplate.getForObject(baseUrl, ExternalApiData.class, from, to);
        return data;
    }
}
