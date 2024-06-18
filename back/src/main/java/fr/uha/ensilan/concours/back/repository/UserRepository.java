package fr.uha.ensilan.concours.back.repository;

import fr.uha.ensilan.concours.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameAndLastname(String username, String lastname);
}
