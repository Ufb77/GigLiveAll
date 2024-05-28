package com.giglive.service;

import com.giglive.model.Imagen;
import com.giglive.repo.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;


    public void setImagenRepository(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public Imagen guardarImagen(ByteArrayResource file) throws IOException {
        Imagen imagen = new Imagen();
        imagen.setImagen(file.getByteArray());
        return imagenRepository.save(imagen);
    }

    public Optional<Imagen> obtenerImagen(int id) {
        return imagenRepository.findById(id);
    }
}
