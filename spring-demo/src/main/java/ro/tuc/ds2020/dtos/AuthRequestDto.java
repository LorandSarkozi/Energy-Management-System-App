package ro.tuc.ds2020.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequestDto {

    private String username;
    private String password;
}