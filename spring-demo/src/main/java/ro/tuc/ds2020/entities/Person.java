package ro.tuc.ds2020.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import ro.tuc.ds2020.constants.UserRole;


import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name="person")
public class Person  implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "age", nullable = false)
    private int age;

    private UserRole role;

    private String password;


    public Person() {
    }

    public Person(String name, String address, int age, UserRole role , String password) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.role = role;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserRole getRole() { return role;}

    public void setRole(UserRole role) { this.role = role;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}
