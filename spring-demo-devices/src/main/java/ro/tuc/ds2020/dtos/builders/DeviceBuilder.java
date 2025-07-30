package ro.tuc.ds2020.dtos.builders;

import org.springframework.beans.factory.annotation.Autowired;
import ro.tuc.ds2020.dtos.DeviceDto;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.util.Optional;

public class DeviceBuilder {

    private final PersonRepository personRepository;

    public DeviceBuilder(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public static DeviceDto toDeviceDto(Device device) {
        return new DeviceDto(device.getDeviceId(), device.getDescription(), device.getAddress(), device.getMaximumHourlyEnergyConsumption(), Optional.ofNullable(device.getPerson()).map(Person::getPersonId).orElse(null));
    }

    public static Device toEntity(DeviceDto deviceDto) {


        return new Device(deviceDto.getDescription(),
                deviceDto.getDeviceId(),
                deviceDto.getAddress(),
                deviceDto.getMaximumHourlyEnergyConsumption()
                );
    }


}
