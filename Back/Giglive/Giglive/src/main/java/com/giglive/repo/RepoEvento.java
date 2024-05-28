package com.giglive.repo;

import com.giglive.model.Evento;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoEvento extends RepositorioGenerico<Evento, Integer>{

    Evento findByNombre(String nombre);
}
