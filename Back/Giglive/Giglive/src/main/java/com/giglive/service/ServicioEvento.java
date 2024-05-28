package com.giglive.service;

import com.giglive.model.Evento;
import com.giglive.repo.RepoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioEvento {

    @Autowired
    RepoEvento repoEvento;

    public Evento save(Evento evento){
        return repoEvento.save(evento);
    }

    public Evento findByNombre(String nombre) {
        return repoEvento.findByNombre(nombre);
    }

    public List<Evento> findAll() {

        return repoEvento.findAll();
    }
}
