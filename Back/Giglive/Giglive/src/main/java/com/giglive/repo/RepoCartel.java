package com.giglive.repo;

import com.giglive.model.Cartel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoCartel extends RepositorioGenerico<Cartel, Integer> {
    //@Query("U")
}
