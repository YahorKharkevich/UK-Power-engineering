package org.uk_energy.ExternalApiController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalApiServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private ExternalApiServiceImpl externalApiService;

    @BeforeEach
    void setUp() {
        externalApiService = new ExternalApiServiceImpl(restTemplate);
        externalApiService.setBaseUrl("http://test-url");
    }

    @Test
    void getData() {
        ExternalApiData expectedData = new ExternalApiData();
        when(restTemplate.getForObject(anyString(), eq(ExternalApiData.class), anyString(), anyString()))
                .thenReturn(expectedData);

        ExternalApiData result = externalApiService.getData("from", "to");

        assertNotNull(result);
        assertEquals(expectedData, result);
        verify(restTemplate).getForObject(eq("http://test-url"), eq(ExternalApiData.class), eq("from"), eq("to"));
    }
}
