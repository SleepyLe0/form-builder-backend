package project.formbuilderbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.JOINED)
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
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private TextQuestion textQuestion;
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private MultipleChoiceQuestion multipleChoiceQuestion;
}