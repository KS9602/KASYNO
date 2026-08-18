package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddUserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
}
