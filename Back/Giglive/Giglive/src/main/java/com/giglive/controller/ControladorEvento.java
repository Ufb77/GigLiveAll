package com.giglive.controller;

import com.giglive.dto.EventoDto;
import com.giglive.model.Evento;
import com.giglive.repo.RepoCartel;
import com.giglive.repo.RepoEvento;
import com.giglive.service.ServicioEvento;
import jdk.jfr.Event;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/evento")
public class ControladorEvento {

    @Autowired
    private ServicioEvento servicioEvento;

    @Autowired
    private RepoEvento repoEvento;


    @PostMapping
    public ResponseEntity<Evento> insertarEvento(@RequestBody Evento evento){
        Evento savedEvento = servicioEvento.save(evento);
         return new ResponseEntity<>(savedEvento, HttpStatus.CREATED);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Evento> obtenerEventoPorNombre(@PathVariable String nombre) {
        Evento evento = servicioEvento.findByNombre(nombre);
        return evento != null ? new ResponseEntity<>(evento, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Evento>> obtenerTodosLosEventos() {
        List<Evento> eventos = servicioEvento.findAll();
        System.out.println("Eventos recuperados: " + eventos.size());
        for(Evento evento: eventos){
            System.out.println(evento.getId_evento() + " " + evento.getNombre());
        }
        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    @GetMapping("/todosDTO")
    public ResponseEntity<List<EventoDto>> obtenerInfoReducidaEventos(){
        List<Evento> eventosCompletos = servicioEvento.findAll();

        List<EventoDto> eventosDto = new ArrayList<>();
        for(Evento event: eventosCompletos){


            eventosDto.add(new EventoDto(event.getNombre(), event.getFecha(),event.getPrecio(), Base64.getEncoder().encodeToString(event.getCartel().getImagen()), event.getId_evento(), event.getCartel().getId_cartel()));

        }

        return new ResponseEntity<>(eventosDto, HttpStatus.OK);
    }

    @GetMapping("/DTO")
    public ResponseEntity<EventoDto> obtenerInfoReducidaEvento(){
        List<Evento> eventosCompletos = servicioEvento.findAll();
        EventoDto eventoDto = new EventoDto();

        for(Evento event: eventosCompletos){

            eventoDto.setEventName(event.getNombre());
            eventoDto.setImage(Base64.getEncoder().encodeToString(event.getCartel().getImagen()));
            eventoDto.setEventDate(event.getFecha());
            eventoDto.setEventPrice(event.getPrecio());
            break;

        }

        return new ResponseEntity<>(eventoDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id_evento}")
    public ResponseEntity<Void> eliminarDesdeEvento(@PathVariable("id_evento") int idEvento){

        repoEvento.eliminarFragmentoDesdeEvento(idEvento);
        repoEvento.eliminarCartelBandasPorEvento(idEvento);
        repoEvento.eliminarBandasDesdeEvento(idEvento);
        repoEvento.eliminarCartelesDesdeEvento(idEvento);
        repoEvento.eliminarEvento(idEvento);
        return ResponseEntity.noContent().build();
    }
}

