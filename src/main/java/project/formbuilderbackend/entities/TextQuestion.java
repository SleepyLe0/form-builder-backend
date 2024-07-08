package project.formbuilderbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "text_questions")
public class TextQuestion extends Question {
}