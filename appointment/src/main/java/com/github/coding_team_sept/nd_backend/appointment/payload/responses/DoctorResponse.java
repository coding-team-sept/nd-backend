package com.github.coding_team_sept.nd_backend.appointment.payload.responses;

public record DoctorResponse(
        Long id,
        String email,
        String name
) {
}
