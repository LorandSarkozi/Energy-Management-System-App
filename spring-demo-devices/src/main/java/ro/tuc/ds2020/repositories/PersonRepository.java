package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {


    Person findByPersonId(UUID id);
    Person findByPersonName(String personName);


}
