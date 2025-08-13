package de.tomreno.assessment.fullstack.backend.domain.person;

import de.tomreno.assessment.fullstack.backend.domain.ColorDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class PersonSaveDto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100, message = "Name must have 2 to 100 characters.")
    private String name;

    @NotNull
    @Size(min = 2, max = 100, message = "Lastname must have 2 to 100 characters.")
    private String lastname;

    @NotNull
    @Size(min = 5, max = 5, message = "Zipcode must have exactly 5 characters.")
    private String zipcode;

    @NotNull
    @Size(min = 2, max = 30, message = "City must have 2 to 30 characters.")
    private String city;

    @NotNull
    private ColorDto color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ColorDto getColor() {
        return color;
    }

    public void setColor(ColorDto color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonSaveDto personDto)) return false;
        return Objects.equals(id, personDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PersonSaveDto{" + "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", city='" + city + '\'' +
                ", color=" + color +
                '}';
    }

}
