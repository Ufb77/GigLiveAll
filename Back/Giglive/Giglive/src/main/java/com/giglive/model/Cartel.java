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
public class Cartel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_cartel;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] imagen;

    @OneToOne()
    @JoinColumn(name = "id_evento")
    private Evento evento;

    @ManyToMany
    @JoinTable(
            name = "cartel_banda",
            joinColumns = @JoinColumn(name = "id_cartel"),
            inverseJoinColumns = @JoinColumn(name = "id_banda")
    )
    private List<Banda> bandas = new ArrayList<>();


}
