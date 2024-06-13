package com.giglive.repo;

import com.giglive.model.Evento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoEvento extends RepositorioGenerico<Evento, Integer>{

    Evento findByNombre(String nombre);

    @Query(value = "DELETE FROM fragmento_cancion WHERE id_banda IN (SELECT id_banda FROM banda WHERE id_banda IN (SELECT id_banda FROM cartel_banda WHERE id_cartel =( SELECT id_cartel FROM cartel WHERE id_evento = :id_evento)))", nativeQuery = true)
    void eliminarFragmentoDesdeEvento(@Param("id_evento")int id);

    @Query(value = "DELETE FROM banda WHERE id_banda IN (SELECT id_banda FROM cartel_banda WHERE id_cartel IN (SELECT id_cartel FROM cartel WHERE id_evento = :id_evento))", nativeQuery = true)
    void eliminarBandasDesdeEvento(@Param("id_evento") int id_evento);

    @Query(value = "DELETE FROM cartel WHERE id_cartel IN (SELECT id_cartel FROM cartel WHERE id_evento = :id_evento)", nativeQuery = true)
    void eliminarCartelesDesdeEvento(@Param("id_evento") int id_evento);

    @Query(value = "DELETE FROM evento WHERE id_evento = :id_evento", nativeQuery = true)
    void eliminarEvento(@Param("id_evento") int id_evento);

    @Query(value = "DELETE FROM cartel_banda WHERE id_banda IN (SELECT id_banda FROM cartel_banda WHERE id_cartel IN (SELECT id_cartel FROM cartel WHERE id_evento = :id_evento))", nativeQuery = true)
    void eliminarCartelBandasPorEvento(@Param("id_evento") int id_evento);
}
