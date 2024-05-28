package com.giglive.controller;

import com.giglive.dto.CartelDto;
import com.giglive.dto.EventoDto;
import com.giglive.model.Cartel;
import com.giglive.model.Evento;
import com.giglive.repo.RepoCartel;
import com.giglive.service.ServicioCartel;
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
@RequestMapping("/cartel")
public class ControladorCartel {

    @Autowired
    ServicioCartel servicioCartel;

    @Autowired
    RepoCartel repoCartel;

    @PostMapping
    public ResponseEntity<Cartel> insertarCartel(@RequestBody Cartel cartel){
        System.out.println("CARTEL ID: " + cartel.getId_cartel() + "EVENTO: " + cartel.getEvento().getId_evento());
        //cartel.getEvento().setId_evento(1);


        return new ResponseEntity<>(servicioCartel.save(cartel), HttpStatus.CREATED);
    }

    @GetMapping("/imagen/{id}")
    public Optional<Cartel> mostrarImagen(@PathVariable int id) {
        Optional<Cartel> cartelOptional = servicioCartel.obtenerCartel(id);
        return cartelOptional;
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<Void> subirImagenCartel(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        Optional<Cartel> optionalCartel = servicioCartel.findById(id);
        if (optionalCartel.isPresent()) {
            try {
                Cartel existingCartel = optionalCartel.get();
                existingCartel.setImagen(file.getBytes());
                servicioCartel.save(existingCartel);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/todosDTO")
    public ResponseEntity<List<CartelDto>> obtenerInfoReducidaCarteles(){
        List<Cartel> cartelesCompletos = repoCartel.findAll();
        List<CartelDto> cartelesDto = new ArrayList<>();
        for(Cartel screen: cartelesCompletos ){



            cartelesDto.add(new CartelDto(screen.getId_cartel(), Base64.getEncoder().encodeToString(screen.getImagen()), screen.getEvento().getId_evento()));

        }

        return new ResponseEntity<>(cartelesDto, HttpStatus.OK);
    }
}



