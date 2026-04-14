package org.example.hotels.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Integer houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}
