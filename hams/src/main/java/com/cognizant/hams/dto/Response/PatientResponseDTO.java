package com.cognizant.hams.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {



    //fields that we are showing to client
    //this dto we are using to send complete patient data back to client


    private Long patientId;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;
//    private String userId;
//    private String status;

}
