package com.giglive.service;

import com.giglive.model.Banda;
import com.giglive.model.Cartel;
import com.giglive.model.Evento;
import com.giglive.repo.RepoCartel;
import com.giglive.repo.RepoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioCartel {

    @Autowired
    RepoCartel repoCartel;

    @Autowired
    ServicioBanda servicioBanda;

    public Cartel save(Cartel cartel){
        // Guarda el cartel
        cartel = repoCartel.save(cartel);



        return cartel;
    }

    public Optional<Cartel> obtenerCartel(int id){
        return repoCartel.findById(id);
    }

    public Optional<Cartel> findById(int id){
        return repoCartel.findById(id);
    }
}
