package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.constants.UserRole;

import java.util.Objects;
import java.util.UUID;

public class PersonDTO extends RepresentationModel<PersonDTO> {
    private UUID id;
    private String name;
    private int age;
    private UserRole role;
    private String address;
    private  String password;

    public PersonDTO() {
    }

    public PersonDTO(UUID id, String name, int age, UserRole role,String address, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.role = role;
        this.address =  address;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserRole getRole() {return role;}
    public void setRole(UserRole role) {this.role = role;}

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {return password;}

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {return address;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return age == personDTO.age &&
                Objects.equals(name, personDTO.name) &&
                Objects.equals(role, personDTO.role) &&
                Objects.equals(address, personDTO.address) &&
                Objects.equals(password, personDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age,role,address,password);
    }
}
