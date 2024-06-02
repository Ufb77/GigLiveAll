package com.giglive.repo;

import com.giglive.model.FragmentoCancion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoFragmentoCancion extends RepositorioGenerico<FragmentoCancion, Integer> {

    @Query( value = "SELECT f.fragmento FROM fragmento_cancion f WHERE f.id_banda = (SELECT b.id_banda FROM banda b WHERE b.nombre_banda = :nombre)",nativeQuery = true)
    byte[] getFragmentoByIdBanda(@Param("nombre")String nombre);

}
