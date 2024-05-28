package com.giglive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDto {

    private String eventName;
    private String eventDate;
    private double eventPrice;
    private String image; // Cambiado a String para codificación Base64
    private int id_eventoDto;
    private int id_cartel; //para buscar el cartel

}
