package org.example.hotels.dto;

public record AddressDto(
        Integer houseNumber,
        String street,
        String city,
        String country,
        String postCode
) {}