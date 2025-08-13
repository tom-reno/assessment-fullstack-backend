package de.tomreno.assessment.fullstack.backend.repository;

import de.tomreno.assessment.fullstack.backend.entity.Color;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "app.database.enabled", havingValue = "true")
public interface PersonJpaRepository extends PersonRepository, JpaRepository<Person, Long> {

    @Query(
            "SELECT p from Person p " +
                    "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', ?1,'%')) " +
                    "OR LOWER(p.lastname) LIKE LOWER(CONCAT('%', ?1,'%')) " +
                    "OR p.zipcode LIKE %?1% " +
                    "OR LOWER(p.city) LIKE LOWER(CONCAT('%', ?1,'%'))"
    )
    Page<Person> findBySearch(String search, Pageable pageable);

    @Query(
            "SELECT p from Person p " +
                    "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', ?1,'%')) " +
                    "OR LOWER(p.lastname) LIKE LOWER(CONCAT('%', ?1,'%')) " +
                    "OR p.zipcode LIKE %?1% " +
                    "OR LOWER(p.city) LIKE LOWER(CONCAT('%', ?1,'%'))) " +
                    "AND p.color = ?2"
    )
    Page<Person> findBySearchAndColor(String search, Color color, Pageable pageable);

}
