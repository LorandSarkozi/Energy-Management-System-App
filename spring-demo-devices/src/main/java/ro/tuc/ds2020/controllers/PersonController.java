package ro.tuc.ds2020.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDto;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.ResponseDto;
import ro.tuc.ds2020.services.PersonService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> signupUser(@RequestBody ResponseDto signupRequest){

        if(personService.existsByPersonName(signupRequest.getAddress())){
            return new ResponseEntity<>("User already exists.", HttpStatus.NOT_ACCEPTABLE);
        }

        PersonDTO personDTO = new PersonDTO();
        personDTO.setPersonId(signupRequest.getId());
        personDTO.setPersonName(signupRequest.getAddress());

        PersonDTO userDto = personService.createPerson(personDTO);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id){
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("update/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable UUID id, @RequestBody PersonDTO personDTO){

        PersonDTO updatedUser = personService.updatePerson(id, personDTO);
        return ResponseEntity.ok(updatedUser);
    }


}
