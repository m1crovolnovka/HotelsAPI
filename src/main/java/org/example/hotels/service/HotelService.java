package org.example.hotels.service;

import org.example.hotels.dto.HotelDetailsDto;
import org.example.hotels.dto.HotelShortDto;

import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelShortDto> getAllHotels();

    HotelDetailsDto getHotelById(Long id);

    List<HotelShortDto> searchHotels(String name, String brand, String city, String country, String amenity);

    HotelShortDto createHotel(HotelDetailsDto dto);

    void addAmenities(Long id, List<String> amenities);

    Map<String, Long> getHistogram(String param);
}