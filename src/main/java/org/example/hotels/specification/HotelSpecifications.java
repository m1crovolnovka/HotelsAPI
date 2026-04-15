package org.example.hotels.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.hotels.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

import java.util.List;

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

    public static Specification<Hotel> hasAmenities(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return null;
            }
            var predicates = amenities.stream()
                    .map(amenity -> cb.isMember(amenity, root.get("amenities")))
                    .toArray(Predicate[]::new);

            return cb.and(predicates);
        };
    }
}
