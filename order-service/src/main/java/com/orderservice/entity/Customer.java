package com.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String middleName;

    @NotNull
    @Min(18)
    @Max(120)
    private Integer age;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 16)
    private String phone;
}
