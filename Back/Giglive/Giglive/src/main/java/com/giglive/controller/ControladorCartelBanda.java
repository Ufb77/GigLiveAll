package com.giglive.controller;

import com.giglive.repo.RepoCartelBanda;
import com.giglive.service.ServicioCartelBanda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cartelbanda")
public class ControladorCartelBanda {

    @Autowired
    RepoCartelBanda repoCartelBanda;

    @Autowired
    ServicioCartelBanda servicioCartelBanda;

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

}
