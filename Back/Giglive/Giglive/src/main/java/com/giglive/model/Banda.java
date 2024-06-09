package com.giglive.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Banda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_banda;

    private String nombreBanda;

    @ManyToMany(mappedBy = "bandas")
    private List<Cartel> carteles;

    @OneToOne(mappedBy = "banda", cascade = CascadeType.ALL, orphanRemoval = true)
    private FragmentoCancion fragmentoCancion;
}
