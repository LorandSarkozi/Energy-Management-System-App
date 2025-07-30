package ro.tuc.ds2020.dtos;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class RequestDto {

    private UUID id;
    private String address;

    public RequestDto(UUID id, String address) {
        this.id = id;
        this.address = address;
    }
}
