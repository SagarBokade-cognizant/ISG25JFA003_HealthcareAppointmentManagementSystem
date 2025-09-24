package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Request.PatientDTO;
import com.cognizant.hams.dto.Response.PatientResponseDTO;
import com.cognizant.hams.security.CustomUserDetailsService;
import com.cognizant.hams.security.JwtTokenUtil;
import com.cognizant.hams.service.Impl.PatientServiceImpl;
import com.cognizant.hams.service.NotificationServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientServiceImpl patientService;

    @MockBean
    private NotificationServiceTest notificationService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private PatientDTO patientDTO;
    private PatientResponseDTO patientResponseDTO;

    @BeforeEach
    void setUp() {
        patientDTO = new PatientDTO("Jane Doe", null, "Female", "9876543210", "jane.doe@example.com", "456 Oak Ave", "A-");
        patientResponseDTO = new PatientResponseDTO(1L, "Jane Doe", "jane.doe@example.com", "9876543210", "456 Oak Ave", "Female", null, "A-");
    }

    @Test
    @WithMockUser
    public void testCreatePatient() throws Exception {
        Mockito.when(patientService.createPatient(any(PatientDTO.class))).thenReturn(patientResponseDTO);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Jane Doe")));
    }

    @Test
    @WithMockUser
    public void testGetPatientById() throws Exception {
        Mockito.when(patientService.getPatientById(1L)).thenReturn(patientResponseDTO);

        mockMvc.perform(get("/api/patients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jane Doe")));
    }

    @Test
    @WithMockUser
    public void testUpdatePatient() throws Exception {
        Mockito.when(patientService.updatePatient(eq(1L), any(PatientDTO.class))).thenReturn(patientResponseDTO);

        mockMvc.perform(put("/api/patients/{patientId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jane Doe")));
    }

    @Test
    @WithMockUser
    public void testDeletePatient() throws Exception {
        Mockito.when(patientService.deletePatient(1L)).thenReturn(patientResponseDTO);

        mockMvc.perform(delete("/api/patients/{patientId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}