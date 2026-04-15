package org.example.hotels.dto;

import java.util.Set;

public record HotelDetailsDto(
        Long id,
        String name,
        String brand,
        String description,
        AddressDto address,
        ContactsDto contacts,
        ArrivalTimeDto arrivalTime,
        Set<String> amenities
) {}
