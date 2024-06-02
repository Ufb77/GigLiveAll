package com.giglive.service;

import com.giglive.model.Banda;
import com.giglive.model.Cartel;
import com.giglive.model.Evento;
import com.giglive.repo.RepoBanda;
import com.giglive.repo.RepoCartel;
import com.giglive.repo.RepoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioBanda {

    @Autowired
    RepoBanda repoBanda;

    @Autowired
    RepoCartel repoCartel;

    public Banda save(Banda banda){
        Banda bandaGuardada = repoBanda.save(banda);

        // Cuando la banda está en un o varios carteles, obtiene la info de los carteles en los que está presente. Después, actualiza las bandas de los carteles con esta nueva banda y guarda(actualiza) el cartel en la bdd
        if (bandaGuardada.getCarteles() != null ) {
            for (Cartel cartel : bandaGuardada.getCarteles()) {
                cartel = repoCartel.findById(cartel.getId_cartel()).orElse(null);
                if(cartel!= null) {
                    cartel.getBandas().add(bandaGuardada);
                    repoCartel.save(cartel);
                }
            }
        }

        return bandaGuardada;
    }

    public Optional<Banda> findBandaById(int bandaId) {

        return repoBanda.findById(bandaId);
    }




}
