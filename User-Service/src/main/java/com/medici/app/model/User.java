package com.medici.app.model;

import java.time.LocalDate;
import java.util.UUID;

public record User(UUID id, String username, String email, String phone, LocalDate dob, LocalDate createdAt) {
}
