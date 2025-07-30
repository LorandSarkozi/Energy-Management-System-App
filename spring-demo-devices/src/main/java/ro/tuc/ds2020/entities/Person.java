package ro.tuc.ds2020.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor

@Table(name="person")
public class Person  implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    private UUID personId;

    @Column(name = "personName", nullable = false)
    private String personName;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device>devices;


    public Person(UUID personId, String personName, List<Device> devices) {
        this.personId = personId;
        this.personName = personName;
        this.devices =  devices;
    }
}
