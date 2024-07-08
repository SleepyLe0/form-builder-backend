package project.formbuilderbackend.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "questionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextQuestion.class, name = "text"),
        @JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = "multiple_choice"),
})
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long id;
    @Column(name = "label", nullable = false)
    private String label;
    @Column(name = "required", nullable = false)
    private Boolean required;
    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;
    @ManyToOne
    @JoinColumn(name = "question_type_id", nullable = false)
    private QuestionType questionType;
}