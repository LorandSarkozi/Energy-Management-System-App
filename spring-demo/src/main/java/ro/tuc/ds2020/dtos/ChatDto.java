package ro.tuc.ds2020.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ChatDto {

    private UUID id;
    private String name;

    public ChatDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}