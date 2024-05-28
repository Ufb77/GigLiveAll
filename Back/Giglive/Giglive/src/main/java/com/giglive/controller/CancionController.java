package com.giglive.controller;

import com.giglive.model.Cancion;
import com.giglive.service.CancionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/canciones")
public class CancionController {

    @Autowired
    private CancionService cancionService;

    @PostMapping("/subir")
    public ResponseEntity<String> subirCancion(@RequestBody MultipartFile file) {
        try {
            ByteArrayResource byteArrayResource = new ByteArrayResource(file.getBytes());
            Cancion cancion = cancionService.guardarCancion(byteArrayResource);
            return ResponseEntity.status(HttpStatus.OK).body("Cancion subida con éxito, ID: " + cancion.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir cancion");
        }
    }

    @GetMapping("/escuchar/{id}")
    public ResponseEntity<ByteArrayResource> escucharCancion(@PathVariable Integer id) {
        return cancionService.obtenerCancion(id)
                .map(cancion -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cancion.mp3\"")
                        .body(new ByteArrayResource(cancion.getSonido())))
                .orElse(ResponseEntity.notFound().build());
    }
}
