package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Request.DoctorAvailabilityDTO;
import com.cognizant.hams.dto.Response.DoctorAndAvailabilityResponseDTO;
import com.cognizant.hams.dto.Response.DoctorAvailabilityResponseDTO;
import com.cognizant.hams.dto.Request.DoctorDTO;
import com.cognizant.hams.dto.Response.DoctorResponseDTO;
import com.cognizant.hams.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    private static final String API_BASE_PATH = "/api/doctors";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DoctorService doctorService;


    private DoctorDTO doctorDTO;
    private DoctorResponseDTO doctorResponseDTO;
    private DoctorAvailabilityDTO doctorAvailabilityDTO;
    private DoctorAvailabilityResponseDTO doctorAvailabilityResponseDTO;
    private DoctorAndAvailabilityResponseDTO doctorAndAvailabilityResponseDTO;

    @BeforeEach
    void setUp() {
//        doctorDTO = new DoctorDTO("Dr. John Doe", "Cardiologist", "john.doe@hospital.com", "1234567890");
//        doctorResponseDTO = new DoctorResponseDTO(1L, "Dr. John Doe", "Cardiologist", "john.doe@hospital.com", "1234567890");
//        doctorAvailabilityDTO = new DoctorAvailabilityDTO(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "Available");
//        doctorAvailabilityResponseDTO = new DoctorAvailabilityResponseDTO(1L, 1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "Available");
//        doctorAndAvailabilityResponseDTO = new DoctorAndAvailabilityResponseDTO("Dr. John Doe", "Cardiologist", "john.doe@hospital.com", "1234567890", List.of(doctorAvailabilityResponseDTO));
    }

    // ------------------------------------------
    // Tests for createDoctor()
    // ------------------------------------------

    @Test
    void testCreateDoctor_Success() throws Exception {
        when(doctorService.createDoctor(any(DoctorDTO.class))).thenReturn(doctorResponseDTO);

        mockMvc.perform(post(API_BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(doctorResponseDTO.getDoctorId()))
                .andExpect(jsonPath("$.name").value(doctorResponseDTO.getDoctorName()));
    }

    // ------------------------------------------
    // Tests for getDoctorById()
    // ------------------------------------------

    @Test
    void testGetDoctorById_Success() throws Exception {
        Long doctorId = 1L;
        when(doctorService.getDoctorById(doctorId)).thenReturn(doctorResponseDTO);

        mockMvc.perform(get(API_BASE_PATH + "/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorResponseDTO.getDoctorId()))
                .andExpect(jsonPath("$.name").value(doctorResponseDTO.getDoctorName()));
    }

    // ------------------------------------------
    // Tests for getAllDoctor()
    // ------------------------------------------

    @Test
    void testGetAllDoctor_Success() throws Exception {
        List<DoctorResponseDTO> doctorList = List.of(doctorResponseDTO);
        when(doctorService.getAllDoctor()).thenReturn(doctorList);

        mockMvc.perform(get(API_BASE_PATH + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(doctorResponseDTO.getDoctorName()));
    }

    // ------------------------------------------
    // Tests for updateDoctor()
    // ------------------------------------------

    @Test
    void testUpdateDoctor_Success() throws Exception {
        Long doctorId = 1L;
        when(doctorService.updateDoctor(eq(doctorId), any(DoctorDTO.class))).thenReturn(doctorResponseDTO);

        mockMvc.perform(put(API_BASE_PATH + "/{doctorId}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorResponseDTO.getDoctorId()))
                .andExpect(jsonPath("$.name").value(doctorResponseDTO.getDoctorName()));
    }

    // ------------------------------------------
    // Tests for deleteDoctor()
    // ------------------------------------------

    @Test
    void testDeleteDoctor_Success() throws Exception {
        Long doctorId = 1L;
        when(doctorService.deleteDoctor(doctorId)).thenReturn(doctorResponseDTO);

        mockMvc.perform(delete(API_BASE_PATH + "/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorResponseDTO.getDoctorId()));
    }

    // ------------------------------------------
    // Tests for searchDoctorsBySpecialization()
    // ------------------------------------------

    @Test
    void testSearchDoctorsBySpecialization_Success() throws Exception {
        String specialization = "Cardiologist";
        List<DoctorResponseDTO> doctorList = List.of(doctorResponseDTO);
        when(doctorService.searchDoctorsBySpecialization(specialization)).thenReturn(doctorList);

        mockMvc.perform(get(API_BASE_PATH + "/specializations")
                        .param("specialization", specialization))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].specialization").value(specialization));
    }

    // ------------------------------------------
    // Tests for searchDoctorsByName()
    // ------------------------------------------

    @Test
    void testSearchDoctorsByName_Success() throws Exception {
        String name = "Dr. John Doe";
        List<DoctorResponseDTO> doctorList = List.of(doctorResponseDTO);
        when(doctorService.searchDoctorsByName(name)).thenReturn(doctorList);

        mockMvc.perform(get(API_BASE_PATH + "/doctorName")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(name));
    }

    // ------------------------------------------
    // Tests for addAvailability()
    // ------------------------------------------

    @Test
    void testAddAvailability_Success() throws Exception {
        Long doctorId = 1L;
        when(doctorService.addAvailability(eq(doctorId), any(DoctorAvailabilityDTO.class))).thenReturn(doctorAvailabilityResponseDTO);

        mockMvc.perform(post(API_BASE_PATH + "/{doctorId}/availability", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorAvailabilityDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(doctorAvailabilityResponseDTO.getDoctorId()))
                .andExpect(jsonPath("$.doctorId").value(doctorAvailabilityResponseDTO.getDoctorId()));
    }

    // ------------------------------------------
    // Tests for getAvailability()
    // ------------------------------------------

    @Test
    void testGetAvailability_Success() throws Exception {
        Long doctorId = 1L;
        List<DoctorAvailabilityResponseDTO> availabilityList = List.of(doctorAvailabilityResponseDTO);
        when(doctorService.getAvailability(doctorId)).thenReturn(availabilityList);

        mockMvc.perform(get(API_BASE_PATH + "/{doctorId}/availability", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].doctorId").value(doctorId));
    }

    // ------------------------------------------
    // Tests for updateAvailabilitySlot()
    // ------------------------------------------

    @Test
    void testUpdateAvailabilitySlot_Success() throws Exception {
        Long doctorId = 1L;
        Long availabilityId = 1L;
        when(doctorService.updateAvailabilitySlot(eq(doctorId), eq(availabilityId), any(DoctorAvailabilityDTO.class))).thenReturn(doctorAvailabilityResponseDTO);

        mockMvc.perform(put(API_BASE_PATH + "/{doctorId}/availability/{availabilityId}", doctorId, availabilityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorAvailabilityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorAvailabilityResponseDTO.getDoctorId()));
    }

    // ------------------------------------------
    // Tests for getAvailableDoctor()
    // ------------------------------------------

    @Test
    void testGetAvailableDoctor_Success() throws Exception {
        String doctorName = "Dr. John Doe";
        List<DoctorAndAvailabilityResponseDTO> responseList = List.of(doctorAndAvailabilityResponseDTO);
        when(doctorService.getAvailableDoctor(doctorName)).thenReturn(responseList);

        mockMvc.perform(get(API_BASE_PATH + "/doctor-availability")
                        .param("name", doctorName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].doctorName").value(doctorName));
    }

    // ------------------------------------------
    // Tests for searchDoctorByName()
    // ------------------------------------------

    @Test
    void testSearchDoctorByName_Success() throws Exception {
        String doctorName = "Dr. John Doe";
        List<DoctorAndAvailabilityResponseDTO> responseList = List.of(doctorAndAvailabilityResponseDTO);
        when(doctorService.searchDoctorByName(doctorName)).thenReturn(responseList);

        mockMvc.perform(get(API_BASE_PATH + "/searchDoctor")
                        .param("name", doctorName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].doctorName").value(doctorName));
    }
}