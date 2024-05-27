package com.github.edocapi.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private int id;
    private String firstName;
    private String lastName;
    private String specialty;
    private String background;
    private Long scheduleId;
    private int experience;
    private double averageRating;
}
