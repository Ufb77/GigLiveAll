package com.giglive.controller;

import com.giglive.dto.BandaDto;
import com.giglive.dto.EventoDto;
import com.giglive.model.Banda;
import com.giglive.model.Evento;
import com.giglive.repo.RepoBanda;
import com.giglive.service.ServicioBanda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/banda")
public class ControladorBanda {

    @Autowired
    private ServicioBanda servicioBanda;


    @Autowired
    private RepoBanda repoBanda;

    @PostMapping
    public ResponseEntity<Banda> insertarBanda(@RequestBody Banda banda){
        return new ResponseEntity<>(servicioBanda.save(banda), HttpStatus.CREATED);
    }




}
