package project.formbuilderbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.formbuilderbackend.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}