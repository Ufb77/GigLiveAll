package com.giglive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartelDto {
    private int id_cartelDto;
    private String imageCartelDto;
    private int id_Evento;
}
