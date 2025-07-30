package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.dtos.DeviceDto;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    Optional<Device> findById(UUID id);

    List<Device> findByPerson(Person person);

}
