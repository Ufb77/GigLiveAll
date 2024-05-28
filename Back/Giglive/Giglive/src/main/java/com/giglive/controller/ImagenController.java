package com.giglive.controller;

import com.giglive.model.Imagen;
import com.giglive.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @PostMapping("/subir")
    public ResponseEntity<String> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            ByteArrayResource byteArrayResource = new ByteArrayResource(file.getBytes());
            Imagen imagen = imagenService.guardarImagen(byteArrayResource);
            return ResponseEntity.status(HttpStatus.OK).body("Imagen subida con éxito, ID: " + imagen.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir imagen");
        }
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<ByteArrayResource> verImagen(@PathVariable Integer id) {
        return imagenService.obtenerImagen(id)
                .map(imagen -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"imagen.jpg\"")
                        .body(new ByteArrayResource(imagen.getImagen())))
                .orElse(ResponseEntity.notFound().build());
    }
}
