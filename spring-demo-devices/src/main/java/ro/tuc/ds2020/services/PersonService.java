package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDto;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO createPerson(PersonDTO personDTO) {
        // Build a Person entity from the DTO
        Person person = new Person();
        person.setPersonId(personDTO.getPersonId());  // or generate a new UUID if needed
        person.setPersonName(personDTO.getPersonName());

        // Save the entity to the database
        Person savedPerson = personRepository.save(person);

        // Log the creation and return the DTO
        LOGGER.info("Created Person with ID: {}", savedPerson.getPersonId());
        return PersonBuilder.toPersonDTO(savedPerson);
    }

    public PersonDTO findPersonByName(String personName) {
        // Retrieve the person by their name
        Person person = personRepository.findByPersonName(personName);

        // If no person is found, throw an exception (or return null based on your requirements)
        if (person == null) {
            LOGGER.warn("Person with name '{}' not found.", personName);
            throw new ResourceNotFoundException("Person not found with name: " + personName);
        }

        // Convert the Person entity to a DTO and return
        return PersonBuilder.toPersonDTO(person);
    }

    public boolean existsByPersonName(String personName) {
        // Check if a person with the given name exists in the database
        return personRepository.findByPersonName(personName) != null;
    }

    public boolean existsByPersonId(UUID personId) {
        return personRepository.findByPersonId(personId) != null;
    }

    public void deletePerson(UUID personId) {
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("Device with ID " + personId + " not found");
        }
        personRepository.deleteById(personId);
    }


    public PersonDTO updatePerson(UUID personId, PersonDTO updatedPersonDto) {

        Person existingPerson = personRepository.findByPersonId(personId);

        existingPerson.setPersonName(updatedPersonDto.getPersonName());

        personRepository.save(existingPerson);

        return PersonBuilder.toPersonDTO(existingPerson);
    }











}
