package ro.tuc.ds2020.dtos;


import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Data
public class DevicePersonDto extends RepresentationModel<DevicePersonDto> {
    private UUID personId;
    private String personName;

    public DevicePersonDto() {
    }

    public DevicePersonDto(UUID id, String name) {
        this.personId = id;
        this.personName = name;
    }

}