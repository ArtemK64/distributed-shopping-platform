package com.orderservice.dto.request;

import lombok.Data;

@Data
public class CustomerDto {

    private String firstName;
    private String lastName;
    private String middleName;
    private Integer age;
    private String email;
    private String phone;
}
