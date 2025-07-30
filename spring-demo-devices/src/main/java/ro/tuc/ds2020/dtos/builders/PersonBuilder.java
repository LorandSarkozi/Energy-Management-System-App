package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.entities.Person;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(person.getPersonId(), person.getPersonName(), person.getDevices());
    }

    public static Person toEntity(PersonDTO personDto) {
        return new Person(personDto.getPersonId(),
                personDto.getPersonName(),
                personDto.getDevices());
    }

}
