package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.constants.UserRole;
import ro.tuc.ds2020.entities.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<Person> findByName(String name);

    @Query("SELECT p FROM Person p WHERE p.address = :address")
    Person findByAddress(@Param("address") String address);

    Person findFirstByAddressAndPassword(String address, String password);
    Person findByRole(UserRole role);

    /**
     * Example: Write Custom Query
     */


}
