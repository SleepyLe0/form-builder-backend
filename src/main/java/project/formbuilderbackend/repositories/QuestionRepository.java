package project.formbuilderbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.formbuilderbackend.entities.question.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}