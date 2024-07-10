package project.formbuilderbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.formbuilderbackend.entities.question.QuestionType;

import java.util.Optional;


public interface QuestionTypeRepository extends JpaRepository<QuestionType, Long> {
    Optional<QuestionType> findByType(String type);
}