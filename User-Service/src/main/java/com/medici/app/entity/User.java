package com.medici.app.entity;

import jakarta.validation.constraints.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_USER", schema = "FINANCE")
public class User {

    @Id
    private UUID uuid;

    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false)
    private String username;

    @Email(message = "Invalid email")
    private String email;

    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Invalid phone number")
    private String phone;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Column(name = "created_at")
    private LocalDate createdAt;

}
