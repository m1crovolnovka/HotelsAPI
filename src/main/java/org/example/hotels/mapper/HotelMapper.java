package org.example.hotels.mapper;

import org.example.hotels.dto.HotelDetailsDto;
import org.example.hotels.dto.HotelShortDto;
import org.example.hotels.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    HotelDetailsDto toDetailsDto(Hotel hotel);

    @Mapping(target = "address", source = "hotel", qualifiedByName = "mapAddressToString")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortDto toShortDto(Hotel hotel);

    Hotel toEntity(HotelDetailsDto dto);

    @Named("mapAddressToString")
    default String mapAddressToString(Hotel hotel) {
        var addr = hotel.getAddress();
        if (addr == null) return "";
        return String.format("%s %s, %s, %s, %s",
                addr.getHouseNumber(), addr.getStreet(), addr.getCity(), addr.getPostCode(), addr.getCountry());
    }
}
