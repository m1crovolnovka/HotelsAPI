package org.example.hotels.specification;

import org.example.hotels.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

public class HotelSpecifications {

    public static Specification<Hotel> hasName(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return (root, query, cb) -> cb.equal(root.get("brand"), brand);
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) -> cb.equal(root.get("address").get("city"), city);
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) -> cb.equal(root.get("address").get("country"), country);
    }

    public static Specification<Hotel> hasAmenity(String amenity) {
        return (root, query, cb) -> {
            Join<Hotel, String> amenitiesJoin = root.join("amenities");
            return cb.equal(amenitiesJoin, amenity);
        };
    }
}
