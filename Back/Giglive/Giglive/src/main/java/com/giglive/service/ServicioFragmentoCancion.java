package com.giglive.service;

import com.giglive.model.Banda;
import com.giglive.model.FragmentoCancion;
import com.giglive.repo.RepoFragmentoCancion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioFragmentoCancion {

    @Autowired
    private RepoFragmentoCancion repoFragmentoCancion;

    public FragmentoCancion save(FragmentoCancion fragmentoCancion){
        return repoFragmentoCancion.save(fragmentoCancion);
    }


    public Optional<FragmentoCancion> findById(int id){
        return repoFragmentoCancion.findById(id);
    }

    public byte[] getFragmentoByNombreBanda(String nombre){
        return repoFragmentoCancion.getFragmentoByIdBanda(nombre);
    }
}
