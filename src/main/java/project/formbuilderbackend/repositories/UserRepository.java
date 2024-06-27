package project.formbuilderbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.formbuilderbackend.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}