package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.constants.UserRole;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.*;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService implements UserDetailsService {


    private PersonRepository personRepository;
    private final RestTemplate restTemplate;


    public PersonService(PersonRepository personRepository, RestTemplate restTemplate) {
        this.personRepository = personRepository;
        this.restTemplate = restTemplate;
    }

    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDetailsDTO findPersonById(UUID id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {

            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }

    public UUID insert(PersonDetailsDTO personDTO) {
        Person person = PersonBuilder.toEntity(personDTO);
        person = personRepository.save(person);

        return person.getId();
    }


    public UserDetails loadUserByUsername(String address) throws UsernameNotFoundException {

        System.out.println("In load..\n");
        System.out.println(address);
        Person optionalPerson = personRepository.findByAddress(address);

        if (optionalPerson == null) throw new UsernameNotFoundException("User not found", null);

        return new org.springframework.security.core.userdetails.User(optionalPerson.getAddress(), optionalPerson.getPassword(),
                new ArrayList<>());
    }

    public PersonDTO createUser(SignupRequestDto signupRequest){

        Person user = new Person();
        user.setAddress(signupRequest.getAddress());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setAge(signupRequest.getAge());
        user.setRole(UserRole.CUSTOMER);


        Person createUser = personRepository.save(user);

        RequestDto requestDto = new RequestDto(user.getId(),user.getAddress());
        ChatDto chatDto = new ChatDto(user.getId(), user.getName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestDto> httpEntity = new HttpEntity<>(requestDto,headers);

        String deviceServiceURL = "http://device-service:8081/person/create";

        restTemplate.exchange(deviceServiceURL, HttpMethod.POST, httpEntity, Void.class);

        PersonDTO userDto = new PersonDTO();
        userDto.setId(createUser.getId());
        return userDto;
    }

    public Boolean hasUserWithAddress(String email){
        if(personRepository.findByAddress(email)!=null){
            return true;
        }
        else{
            return false;
        }
    }
    public PersonDTO updatePerson(UUID id, PersonDetailsDTO updatedPersonDTO) {
        Person personToUpdate = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id: " + id + " not found"));

        personToUpdate.setName(updatedPersonDTO.getName());
        personToUpdate.setAddress(updatedPersonDTO.getAddress());
        personToUpdate.setAge(updatedPersonDTO.getAge());
        if (updatedPersonDTO.getPassword() != null && !updatedPersonDTO.getPassword().isEmpty()) {
            personToUpdate.setPassword(new BCryptPasswordEncoder().encode(updatedPersonDTO.getPassword()));
        }
        personToUpdate.setRole(updatedPersonDTO.getRole());

        personRepository.save(personToUpdate);

        String otherServiceUrl = "http://device-service:8081/person/update/{id}";

        DevicePersonDto personDTO = new DevicePersonDto();
        personDTO.setPersonId(id);
        personDTO.setPersonName(updatedPersonDTO.getAddress());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DevicePersonDto> requestEntity = new HttpEntity<>(personDTO, headers);

        restTemplate.put(otherServiceUrl, requestEntity, id);

        return PersonBuilder.toPersonDTO(personToUpdate);
    }

    public void deletePerson(UUID id) {

        if (!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("Person with id: " + id + " not found");
        }


        String deviceServiceGetDevicesURL = "http://localhost:8081/devices/find/{personId}";
        ResponseEntity<List<DeviceDto>> response = restTemplate.exchange(
                deviceServiceGetDevicesURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeviceDto>>() {},
                id
        );

        List<DeviceDto> devices = response.getBody();

        if (devices != null && !devices.isEmpty()) {
            for (DeviceDto device : devices) {
                device.setPersonId(null);  // Assuming 'personId' represents the user
                String deviceUpdateURL = "http://device-service:8081/devices/{deviceId}";
                restTemplate.put(deviceUpdateURL, device, device.getDeviceId());
            }
        }

        String deviceServiceDeleteUserURL = "http://device-service:8081/person/delete/{id}";
        restTemplate.exchange(
                deviceServiceDeleteUserURL,
                HttpMethod.DELETE,
                null,
                Void.class,
                id
        );

        personRepository.deleteById(id);
    }

}
