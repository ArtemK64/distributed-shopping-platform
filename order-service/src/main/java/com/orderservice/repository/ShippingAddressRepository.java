package com.orderservice.repository;

import com.orderservice.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    Optional<ShippingAddress> findByCountryAndCityAndStreetAndHouseAndPostalCode(
            String country,
            String city,
            String street,
            String house,
            String postalCode
    );
}
