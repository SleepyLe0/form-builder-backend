package project.formbuilderbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "text_questions")
public class TextQuestion extends Question {
    @OneToOne
    @MapsId
    @JoinColumn(name = "question_id")
    private Question question;
}