package com.giglive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FragmentoDto {
    private int id_fragmentoDto;
    private String cancionDto;
    private int id_banda;
}
