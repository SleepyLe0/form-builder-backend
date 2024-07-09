package project.formbuilderbackend.entities.question;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "multiple_choice_questions")
public class MultipleChoiceQuestion extends Question {
    @Column(name = "multiple_answers", nullable = false)
    private Boolean multipleAnswers;
    @Column(name = "multiple_type")
    @Enumerated(EnumType.STRING)
    private MultipleChooseType multipleType;
    @Column(name = "choose_limit")
    private Integer chooseLimit;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices;
    @OneToOne
    @MapsId
    @JoinColumn(name = "question_id")
    private Question question;
}