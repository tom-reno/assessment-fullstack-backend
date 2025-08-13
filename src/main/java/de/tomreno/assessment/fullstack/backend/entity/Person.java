package de.tomreno.assessment.fullstack.backend.entity;

import jakarta.persistence.*;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSeq")
    @SequenceGenerator(name = "personSeq", sequenceName = "person_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String lastname;

    private String zipcode;

    private String city;

    @Enumerated(EnumType.STRING)
    private Color color;

    public Person(long id) {
        this.id = id;
    }

    public Person() {
    }

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int compareWith(Person other, Sort.Order order) {
        return switch (order.getProperty()) {
            case "id" -> order.getDirection().isAscending() ? this.getId().compareTo(other.getId()) :
                    other.getId().compareTo(this.getId());
            case "name" -> order.getDirection().isAscending() ? this.getName().compareToIgnoreCase(other.getName()) :
                    other.getName().compareToIgnoreCase(this.getName());
            case "lastname" ->
                    order.getDirection().isAscending() ? this.getLastname().compareToIgnoreCase(other.getLastname()) :
                            other.getLastname().compareToIgnoreCase(this.getLastname());
            case "zipcode" -> order.getDirection().isAscending() ? this.getZipcode().compareTo(other.getZipcode()) :
                    other.getZipcode().compareTo(this.getZipcode());
            case "city" -> order.getDirection().isAscending() ? this.getCity().compareToIgnoreCase(other.getCity()) :
                    other.getCity().compareToIgnoreCase(this.getCity());
            case "color" -> {
                String color = this.getColor().getName();
                yield order.getDirection().isAscending() ? color.compareToIgnoreCase(other.getColor().getName()) :
                        other.getColor().getName().compareToIgnoreCase(color);
            }
            default -> 0;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", city='" + city + '\'' +
                ", color=" + color +
                '}';
    }

}
