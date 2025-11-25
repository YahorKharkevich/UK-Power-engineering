package org.uk_energy.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.uk_energy.Service.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAvgEnergyMix() throws Exception {
        when(userService.getAvgEnergyMix()).thenReturn(Collections.singletonList("test"));

        mockMvc.perform(get("/api/v1/avgEnergyMix"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"test\"]"));
    }

    @Test
    void getBestInterval() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("key", "value");
        when(userService.getBestInterval(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/bestInterval").param("duration", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"key\":\"value\"}"));
    }
}
