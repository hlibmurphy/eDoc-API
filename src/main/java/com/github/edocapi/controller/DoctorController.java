package com.github.edocapi.controller;

import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.dto.AvailableSlotsDto;
import com.github.edocapi.dto.CreateAppointmentRequestDto;
import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.DoctorDtoWithoutScheduleId;
import com.github.edocapi.dto.DoctorScheduleDto;
import com.github.edocapi.dto.ReviewDto;
import com.github.edocapi.dto.UpdateScheduleRequestDto;
import com.github.edocapi.model.User;
import com.github.edocapi.service.AppointmentService;
import com.github.edocapi.service.DoctorScheduleService;
import com.github.edocapi.service.DoctorService;
import com.github.edocapi.service.ReviewService;
import com.github.edocapi.service.impl.TimeSlotServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Validated
public class DoctorController {
    private final DoctorService doctorService;
    private final ReviewService reviewService;
    private final DoctorScheduleService doctorScheduleService;
    private final TimeSlotServiceImpl timeSlotService;
    private final AppointmentService appointmentService;

    @GetMapping
    public List<DoctorDtoWithoutScheduleId> getAllDoctors(Pageable pageable) {
        return doctorService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public DoctorDtoWithoutScheduleId getDoctorById(@PathVariable @Positive Long id) {
        return doctorService.findById(id);
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewDto> getReviewsByDoctorId(@PathVariable @Positive Long id,
                                                Pageable pageable) {
        return reviewService.findByDoctorId(id, pageable);
    }

    @GetMapping("/{id}/available-slots")
    public AvailableSlotsDto getAvailableTimeSlotsByDoctorId(
            @PathVariable Long id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return timeSlotService.findAvailableSlots(id, date);
    }

    @GetMapping("/{id}/appointments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AppointmentDto> getAppointmentsByDoctorId(
            @PathVariable @Positive Long id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return appointmentService.findAppointmentsByDoctorIdAndDate(id, date);
    }

    @GetMapping("/{id}/schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DoctorScheduleDto getDoctorSchedule(@PathVariable @Positive Long id) {
        return doctorScheduleService.findByDoctorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto createDoctor(@Valid @RequestBody CreateDoctorRequestDto doctorRequestDto) {
        return doctorService.save(doctorRequestDto);
    }

    @PostMapping("/{id}/appointment")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto createAppointment(
            @PathVariable @Positive Long id,
            Authentication authentication,
            @Valid @RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        User user = (User) authentication.getPrincipal();
        return appointmentService.save(id, user, createAppointmentRequestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DoctorDtoWithoutScheduleId updateDoctor(
            @PathVariable @Positive Long id,
            @Valid @RequestBody
            CreateDoctorRequestDto doctorRequestDto) {
        return doctorService.update(id, doctorRequestDto);
    }

    @PutMapping("/{id}/schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DoctorScheduleDto updateSchedule(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateScheduleRequestDto scheduleRequestDto) {
        return doctorScheduleService.update(id, scheduleRequestDto);
    }
}
