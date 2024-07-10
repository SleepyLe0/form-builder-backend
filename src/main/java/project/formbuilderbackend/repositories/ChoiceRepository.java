package project.formbuilderbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.formbuilderbackend.entities.question.Choice;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    List<Choice> findByQuestionId(Long questionId);
}