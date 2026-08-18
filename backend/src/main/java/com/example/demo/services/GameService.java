package com.example.demo.services;

import com.example.demo.DTO.CardsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    public List<CardsDTO> tasuj(){
        return List.of(
                new CardsDTO(1,"KA","2"),
                new CardsDTO(2,"P","A"),
                new CardsDTO(3,"KI","K")
        );
    }
}
