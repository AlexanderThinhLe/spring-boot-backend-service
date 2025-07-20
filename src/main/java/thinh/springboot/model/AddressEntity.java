package thinh.springboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_address")
@Getter
@Setter
public class AddressEntity extends AbstractEntity<Long> {
    @Column(name = "apartment_number", length = 255)
    private String apartmentNumber;

    @Column(name = "floor", length = 255)
    private String floor;

    @Column(name = "building", length = 255)
    private String building;

    @Column(name = "street_number", length = 255)
    private String streetNumber;

    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "city", length = 255)
    private String city;

    @Column(name = "country", length = 255)
    private String country;

    @Column(name = "address_type")
    private Integer addressType;

    @Column(name = "user_id")
    private Long userId;
}
