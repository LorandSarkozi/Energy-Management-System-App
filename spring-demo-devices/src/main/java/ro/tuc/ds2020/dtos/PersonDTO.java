package ro.tuc.ds2020.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.Device;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Data
public class PersonDTO extends RepresentationModel<PersonDTO> {
    private UUID personId;
    private String personName;
    private List<Device> devices;

    public PersonDTO() {
    }

    public PersonDTO(UUID id, String name, List<Device> devices) {
        this.personId = id;
        this.personName = name;
        this.devices = devices;
    }

    public PersonDTO(UUID id, String name) {
        this.personId = id;
        this.personName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return devices == personDTO.devices &&
                Objects.equals(personName, personDTO.personName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personName, devices);
    }
}
