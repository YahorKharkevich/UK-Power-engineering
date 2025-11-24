package org.uk_energy.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.uk_energy.ExternalApiController.ExternalApiData;
import org.uk_energy.ExternalApiController.ExternalApiService;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private ExternalApiService externalApiService;

    @Mock
    private ExternalApiData externalApiData;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(externalApiService);
    }

    @Test
    void getAvgEnergyMix() {
        when(externalApiService.getData(anyString(), anyString())).thenReturn(externalApiData);

        Map<String, Object> summaryMap = new HashMap<>();
        summaryMap.put("wind", 50.0);
        when(externalApiData.getSummary(any(ZonedDateTime.class), eq(24L))).thenReturn(summaryMap);

        Object result = userService.getAvgEnergyMix();

        assertNotNull(result);
        assertTrue(result instanceof List);
        List<?> list = (List<?>) result;
        assertEquals(3, list.size());
        verify(externalApiService).getData(anyString(), anyString());
        verify(externalApiData, times(3)).getSummary(any(ZonedDateTime.class), eq(24L));
    }

    @Test
    void getBestInterval() {
        long duration = 2;
        when(externalApiService.getData(anyString(), anyString())).thenReturn(externalApiData);

        Map<String, Object> lowCleanMap = new HashMap<>();
        lowCleanMap.put("clean energy percent", 10.0);
        lowCleanMap.put("from", ZonedDateTime.now());
        lowCleanMap.put("to", ZonedDateTime.now().plusHours(duration));

        Map<String, Object> highCleanMap = new HashMap<>();
        highCleanMap.put("clean energy percent", 90.0);
        highCleanMap.put("from", ZonedDateTime.now());
        highCleanMap.put("to", ZonedDateTime.now().plusHours(duration));

        when(externalApiData.getSummary(any(ZonedDateTime.class), eq(duration)))
                .thenReturn(lowCleanMap)
                .thenReturn(highCleanMap)
                .thenReturn(lowCleanMap);

        Object result = userService.getBestInterval(duration);

        assertNotNull(result);
        assertTrue(result instanceof Map);
        Map<?, ?> map = (Map<?, ?>) result;
        assertEquals(90.0, map.get("clean energy percent"));
        verify(externalApiService).getData(anyString(), anyString());
        verify(externalApiData, atLeastOnce()).getSummary(any(ZonedDateTime.class), eq(duration));
    }

    @Test
    void getBestInterval_NoData() {
        long duration = 2;
        when(externalApiService.getData(anyString(), anyString())).thenReturn(externalApiData);

        Map<String, Object> emptyMap = new HashMap<>();
        when(externalApiData.getSummary(any(ZonedDateTime.class), eq(duration))).thenReturn(emptyMap);

        Object result = userService.getBestInterval(duration);

        assertNotNull(result);
        assertTrue(result instanceof Map);
        Map<?, ?> map = (Map<?, ?>) result;
        assertTrue(map.isEmpty());
        verify(externalApiService).getData(anyString(), anyString());
        verify(externalApiData, atLeastOnce()).getSummary(any(ZonedDateTime.class), eq(duration));
    }

    @Test
    void getBestInterval_InvalidDuration() {
        assertThrows(IllegalArgumentException.class, () -> userService.getBestInterval(0));
        assertThrows(IllegalArgumentException.class, () -> userService.getBestInterval(7));
    }
}