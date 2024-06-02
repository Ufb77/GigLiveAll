package com.giglive.controller;


import com.giglive.dto.EventoDto;
import com.giglive.dto.FragmentoDto;
import com.giglive.model.Banda;
import com.giglive.model.Evento;
import com.giglive.model.FragmentoCancion;
import com.giglive.repo.RepoFragmentoCancion;
import com.giglive.service.ServicioBanda;
import com.giglive.service.ServicioFragmentoCancion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fragmentoCancion")
public class ControladorFragmentoCancion {
    @Autowired
    ServicioFragmentoCancion servicioFragmentoCancion;

    @Autowired
    ServicioBanda servicioBanda;

    @Autowired
    RepoFragmentoCancion repoFragmentoCancion;

    @PostMapping
    public ResponseEntity<FragmentoCancion> insertarFragmentoCancion(@RequestBody FragmentoCancion fragmentoCancion){
        return new ResponseEntity<>(servicioFragmentoCancion.save(fragmentoCancion), HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> subirFragmentoCancion(@RequestParam("bandaId") int bandaId, @RequestParam("file") MultipartFile file) {
        Optional<Banda> optionalBanda = servicioBanda.findBandaById(bandaId);
        if (optionalBanda.isPresent()) {
            try {
                Banda banda = optionalBanda.get();
                FragmentoCancion fragmentoCancion = new FragmentoCancion();
                fragmentoCancion.setFragmento(file.getBytes());
                fragmentoCancion.setBanda(banda);
                servicioFragmentoCancion.save(fragmentoCancion);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todosDTO")
    public ResponseEntity<List<FragmentoDto>> obtenerInfoReducidaEventos(){
        List<FragmentoCancion> fragmentosCompletos = repoFragmentoCancion.findAll();

        List<FragmentoDto> fragmentosDto = new ArrayList<>();

        for(FragmentoCancion fragment: fragmentosCompletos){

            fragmentosDto.add(new FragmentoDto(fragment.getId(), Base64.getEncoder().encodeToString(fragment.getFragmento()), fragment.getBanda().getId_banda()));

        }

        return new ResponseEntity<>(fragmentosDto, HttpStatus.OK);
    }

    @GetMapping("/obtenerFragmento/{nombreBanda}")
    public ResponseEntity<byte[]>getFragmentoByNombreBanda(@PathVariable("nombreBanda") String nombreBanda){
        return new ResponseEntity<>(servicioFragmentoCancion.getFragmentoByNombreBanda(nombreBanda), HttpStatus.OK);
    }

//    @GetMapping("/obtenerFragmento/{nombreBanda}")
//    public ResponseEntity<String> getFragmentoByNombreBanda(@PathVariable("nombreBanda") String nombreBanda){
//        byte[] fragmentoBytes = servicioFragmentoCancion.getFragmentoByNombreBanda(nombreBanda);
//        String fragmentoBase64 = Base64.getEncoder().encodeToString(fragmentoBytes);
//        return new ResponseEntity<>(fragmentoBase64, HttpStatus.OK);
//    }
}
