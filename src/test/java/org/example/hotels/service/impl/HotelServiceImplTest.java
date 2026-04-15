package org.example.hotels.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.example.hotels.dto.HotelDetailsDto;
import org.example.hotels.dto.HotelShortDto;
import org.example.hotels.entity.Hotel;
import org.example.hotels.mapper.HotelMapper;
import org.example.hotels.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelShortDto shortDto;
    private HotelDetailsDto detailsDto;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setAmenities(new HashSet<>(Arrays.asList("WiFi", "Parking")));

        shortDto = new HotelShortDto(1L, "Test Hotel", "Desc", "Address", "123");
        detailsDto = new HotelDetailsDto(1L, "Test Hotel", "Brand", "Desc", null, null, null, Set.of("WiFi"));
    }

    @Test
    @DisplayName("Should return all hotels mapped to ShortDto")
    void getAllHotels_Success() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(hotelMapper.toShortDto(hotel)).thenReturn(shortDto);

        List<HotelShortDto> result = hotelService.getAllHotels();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Test Hotel");
        verify(hotelRepository).findAll();
    }

    @Test
    @DisplayName("Should return hotel details by ID")
    void getHotelById_Success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailsDto(hotel)).thenReturn(detailsDto);

        HotelDetailsDto result = hotelService.getHotelById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should create hotel and return its ShortDto")
    void createHotel_Success() {
        when(hotelMapper.toEntity(detailsDto)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toShortDto(hotel)).thenReturn(shortDto);

        HotelShortDto result = hotelService.createHotel(detailsDto);

        assertThat(result).isNotNull();
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    @DisplayName("Should add amenities to existing hotel")
    void addAmenities_Success() {
        List<String> newAmenities = List.of("Gym", "Pool");
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        hotelService.addAmenities(1L, newAmenities);

        assertThat(hotel.getAmenities()).containsAll(newAmenities);
        verify(hotelRepository).save(hotel);
    }

    @Test
    @DisplayName("Should return correct histogram for brand")
    void getHistogram_Brand_Success() {
        List<Object[]> mockResult = List.of(new Object[]{"Hilton", 5L}, new Object[]{"Marriott", 3L});
        when(hotelRepository.countByBrand()).thenReturn(mockResult);

        Map<String, Long> histogram = hotelService.getHistogram("brand");

        assertThat(histogram).hasSize(2);
        assertThat(histogram.get("Hilton")).isEqualTo(5L);
        assertThat(histogram.get("Marriott")).isEqualTo(3L);
    }

    @Test
    @DisplayName("Should search hotels with specifications")
    void searchHotels_WithParams_Success() {
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(List.of(hotel));
        when(hotelMapper.toShortDto(hotel)).thenReturn(shortDto);

        List<HotelShortDto> result = hotelService.searchHotels("Test", null, null, null, null);

        assertThat(result).isNotEmpty();
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Should throw exception when hotel not found")
    void getHotelById_NotFound_ThrowsException() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> hotelService.getHotelById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Hotel not found");
    }
}
