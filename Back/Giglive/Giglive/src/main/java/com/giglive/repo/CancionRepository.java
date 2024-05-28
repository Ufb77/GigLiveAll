package com.giglive.repo;

import com.giglive.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancionRepository extends JpaRepository<Cancion, Integer> {
}
