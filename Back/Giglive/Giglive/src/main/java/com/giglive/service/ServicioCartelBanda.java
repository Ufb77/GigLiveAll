package com.giglive.service;

import com.giglive.repo.RepoCartelBanda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioCartelBanda {

    @Autowired
    private RepoCartelBanda repoCartelBanda;


    public List<Integer> getIdsCartel() {
        return repoCartelBanda.idsCartel();
    }


    public List<Integer> getIdsBandas() {
        return repoCartelBanda.idsBandas();
    }


    public List<Integer> getCartelIdsByBandaId(int idBanda) {
        return repoCartelBanda.findCartelIdsByBandaId(idBanda);
    }


    public List<Integer> getBandaIdsByCartelId(int idcartel) {
        return repoCartelBanda.findBandaIdsByCartelId(idcartel);
    }
}
