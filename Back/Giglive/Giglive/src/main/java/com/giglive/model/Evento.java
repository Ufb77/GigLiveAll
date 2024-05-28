package com.giglive.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_evento;

    private String nombre;

    private String fecha;

    private double precio;

    @OneToOne(mappedBy = "evento", cascade = CascadeType.ALL)
    private Cartel cartel;
}
