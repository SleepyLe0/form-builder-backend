package project.formbuilderbackend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.entities.*;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionDto {
    Long id;
    String label;
    Boolean required;
    @JsonIgnore
    QuestionType questionType;
    @JsonIgnore
    TextQuestion textQuestion;
    @JsonIgnore
    MultipleChoiceQuestion multipleChoiceQuestion;
    String type;
    Boolean multipleAnswers;
    MultipleChooseType multipleType;
    Integer chooseLimit;
    List<String> choices;

    public String getType() {
        return questionType.getType();
    }

    public Boolean getMultipleAnswers() {
        if (multipleChoiceQuestion == null) return null;
        return multipleChoiceQuestion.getMultipleAnswers();
    }

    public MultipleChooseType getMultipleType() {
        if (multipleChoiceQuestion == null) return null;
        return multipleChoiceQuestion.getMultipleType();
    }

    public Integer getChooseLimit() {
        if (multipleChoiceQuestion == null) return null;
        return multipleChoiceQuestion.getChooseLimit();
    }

    public List<String> getChoices() {
        if (multipleChoiceQuestion == null) return null;
        return multipleChoiceQuestion.getChoices().stream().map(Choice::getLabel).toList();
    }
}