package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Request.MedicalRecordDTO;
import com.cognizant.hams.dto.Response.MedicalRecordResponseDTO;
import com.cognizant.hams.security.CustomUserDetailsService;
import com.cognizant.hams.security.JwtTokenUtil;
import com.cognizant.hams.service.MedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalRecordService medicalRecordService;

    // Mock security beans
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @WithMockUser
    public void testCreateRecord() throws Exception {
        MedicalRecordDTO requestDto = new MedicalRecordDTO(1001L, 1L, 101L, "Checkup", "Healthy", "Notes");
        MedicalRecordResponseDTO responseDto = new MedicalRecordResponseDTO();
        responseDto.setRecordId(1L);
        responseDto.setPatientName("Test Patient");

        Mockito.when(medicalRecordService.createRecord(any(MedicalRecordDTO.class))).thenReturn(responseDto);
        mockMvc.perform(post("/api/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordId", is(1)))
                .andExpect(jsonPath("$.patientName", is("Test Patient")));
    }

    @Test
    @WithMockUser
    public void testGetRecordsForPatient() throws Exception {
        MedicalRecordResponseDTO responseDto = new MedicalRecordResponseDTO();
        responseDto.setRecordId(1L);
        Mockito.when(medicalRecordService.getRecordsForPatient(1L))
                .thenReturn(Collections.singletonList(responseDto));

        mockMvc.perform(get("/api/medical-records/patient/{patientId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser
    public void testGetRecordsForDoctor() throws Exception {
        MedicalRecordResponseDTO responseDto = new MedicalRecordResponseDTO();
        responseDto.setRecordId(1L);
        Mockito.when(medicalRecordService.getRecordsForDoctor(101L))
                .thenReturn(Collections.singletonList(responseDto));

        mockMvc.perform(get("/api/medical-records/doctor/{doctorId}", 101L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}