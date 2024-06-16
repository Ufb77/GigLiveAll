package com.giglive.controller;

import com.giglive.repo.RepoCartelBanda;
import com.giglive.service.ServicioCartelBanda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartelbanda")
public class ControladorCartelBanda {

    @Autowired
    private RepoCartelBanda repoCartelBanda;

    @Autowired
    private ServicioCartelBanda servicioCartelBanda;

    @GetMapping("/idsCartel")
    public List<Integer> getIdsCartel() {
        return repoCartelBanda.idsCartel();
    }

    @GetMapping("/idsBandas")
    public List<Integer> getIdsBandas() {
        return repoCartelBanda.idsBandas();
    }

    @GetMapping("/cartelIdsByBandaId")
    public List<Integer> getCartelIdsByBandaId(@RequestBody int idBanda) {
        return repoCartelBanda.findCartelIdsByBandaId(idBanda);
    }

    @GetMapping("/bandaIdsByCartelId")
    public List<Integer> getBandaIdsByCartelId(@RequestBody Integer idcartel) {
        return repoCartelBanda.findBandaIdsByCartelId(idcartel);
    }

    @GetMapping("/nombresPorCartelId/{id_cartel}")
    public ResponseEntity<List<String>> obtenerNombresBandasPorCartelId(@PathVariable("id_cartel") Integer idCartel) {
        List<String> nombresBandas = servicioCartelBanda.obtenerNombresBandasPorCartelId(idCartel);
        return ResponseEntity.ok(nombresBandas);
    }

}
