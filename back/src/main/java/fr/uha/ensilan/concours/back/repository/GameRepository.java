package fr.uha.ensilan.concours.back.repository;

import fr.uha.ensilan.concours.back.domain.game.Game;
import fr.uha.ensilan.concours.back.domain.game.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findByDiffusion(Boolean diffusion);
}
