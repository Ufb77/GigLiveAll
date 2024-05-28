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
public class FragmentoCancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] fragmento;

    @OneToOne
    @JoinColumn(name = "id_banda")
    private Banda banda;
}
