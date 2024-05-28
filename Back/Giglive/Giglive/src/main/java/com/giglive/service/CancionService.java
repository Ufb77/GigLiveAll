package com.giglive.service;

import com.giglive.model.Cancion;
import com.giglive.model.Imagen;
import com.giglive.repo.CancionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class CancionService {

    @Autowired
    private CancionRepository cancionRepository;


    public void setCancionRepository(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    public Cancion guardarCancion(ByteArrayResource cancionResource) throws IOException {
        Cancion cancion = new Cancion();
        cancion.setSonido(cancionResource.getByteArray());
        return cancionRepository.save(cancion);
    }

    public Optional<Cancion> obtenerCancion(int id) {
        return cancionRepository.findById(id);
    }
}
