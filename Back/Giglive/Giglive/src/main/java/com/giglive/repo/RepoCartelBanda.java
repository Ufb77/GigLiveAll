package com.giglive.repo;

import com.giglive.dto.CartelBandaDto;
import com.giglive.model.Cartel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoCartelBanda extends JpaRepository<Cartel,Integer > {
    @Query(value = "SELECT id_cartel FROM cartel_banda", nativeQuery = true)
    public List<Integer> idsCartel();

    @Query(value = "SELECT id_banda FROM cartel_banda", nativeQuery = true)
    public List<Integer> idsBandas();

    @Query(value = "SELECT id_cartel FROM cartelbanda WHERE id_cartel = :id_banda", nativeQuery = true)
    List<Integer> findCartelIdsByBandaId(@Param("id_banda") Integer id_banda);

    @Query(value = "SELECT id_banda FROM cartelbanda WHERE id_banda = :id_cartel", nativeQuery = true)
    List<Integer> findBandaIdsByCartelId(@Param("id_cartel") Integer id_cartel);

}
