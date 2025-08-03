package com.orderservice.dto.request;

import lombok.Data;

@Data
public class ShippingAddressDto {

    private String country;
    private String city;
    private String street;
    private String house;
    private String postalCode;
}
