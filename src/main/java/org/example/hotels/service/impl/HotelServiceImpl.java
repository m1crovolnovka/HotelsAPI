package org.example.hotels.service.impl;

import org.example.hotels.dto.HotelDetailsDto;
import org.example.hotels.dto.HotelShortDto;
import org.example.hotels.entity.Hotel;
import org.example.hotels.mapper.HotelMapper;
import org.example.hotels.repository.HotelRepository;
import org.example.hotels.service.HotelService;
import org.example.hotels.specification.HotelSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public HotelServiceImpl(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDetailsDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortDto> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        Specification<Hotel> spec = Specification.unrestricted();
        if (name != null && !name.isBlank()) {
            spec = spec.and(HotelSpecifications.hasName(name));
        }
        if (brand != null && !brand.isBlank()) {
            spec = spec.and(HotelSpecifications.hasBrand(brand));
        }
        if (city != null && !city.isBlank()) {
            spec = spec.and(HotelSpecifications.hasCity(city));
        }
        if (country != null && !country.isBlank()) {
            spec = spec.and(HotelSpecifications.hasCountry(country));
        }
        if (amenities != null && !amenities.isEmpty()) {
            spec = spec.and(HotelSpecifications.hasAmenities(amenities));
        }
        return hotelRepository.findAll(spec).stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Override
    @Transactional
    public HotelShortDto createHotel(HotelDetailsDto dto) {
        Hotel hotel = hotelMapper.toEntity(dto);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toShortDto(savedHotel);
    }

    @Override
    @Transactional
    public void addAmenities(Long id, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotel.getAmenities().addAll(amenities);
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String param) {
        List<Object[]> results = switch (param.toLowerCase()) {
            case "brand" -> hotelRepository.countByBrand();
            case "city" -> hotelRepository.countByCity();
            case "country" -> hotelRepository.countByCountry();
            case "amenities" -> hotelRepository.countByAmenities();
            default -> throw new IllegalArgumentException("Unknown parameter: " + param);
        };

        return results.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));
    }
}
