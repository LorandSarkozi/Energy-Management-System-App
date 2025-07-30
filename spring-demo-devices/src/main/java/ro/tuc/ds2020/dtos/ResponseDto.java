package ro.tuc.ds2020.dtos;


import lombok.Data;

import java.util.UUID;

@Data
public class ResponseDto {

    private UUID id;
    private String address;


}
