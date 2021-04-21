package io.github.glaucials.lutalivre.repository;

import io.github.glaucials.lutalivre.entity.Lutador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LutadorRepository extends JpaRepository<Lutador, Integer> {

    @Query("select count(*) from Lutador where vivo = 'true'")
    int countAliveFighters();

    @Query("select count(*) from Lutador where vivo = 'false'")
    int countDeadFighters();
}
