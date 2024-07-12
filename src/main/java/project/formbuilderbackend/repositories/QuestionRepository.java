package project.formbuilderbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.formbuilderbackend.entities.question.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}