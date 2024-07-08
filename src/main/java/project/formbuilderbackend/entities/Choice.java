package project.formbuilderbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "choices")
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id", nullable = false)
    private Long id;
    @Column(name = "label", nullable = false)
    private String label;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private MultipleChoiceQuestion question;
}