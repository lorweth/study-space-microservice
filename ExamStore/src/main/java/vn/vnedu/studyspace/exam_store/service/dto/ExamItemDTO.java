package vn.vnedu.studyspace.exam_store.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.exam_store.domain.ExamItem} entity.
 */
public class ExamItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer numOfQuestion;

    private QuestionGroupDTO questionGroup;

    private ExamDTO exam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumOfQuestion() {
        return numOfQuestion;
    }

    public void setNumOfQuestion(Integer numOfQuestion) {
        this.numOfQuestion = numOfQuestion;
    }

    public QuestionGroupDTO getQuestionGroup() {
        return questionGroup;
    }

    public void setQuestionGroup(QuestionGroupDTO questionGroup) {
        this.questionGroup = questionGroup;
    }

    public ExamDTO getExam() {
        return exam;
    }

    public void setExam(ExamDTO exam) {
        this.exam = exam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamItemDTO)) {
            return false;
        }

        ExamItemDTO examItemDTO = (ExamItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamItemDTO{" +
            "id=" + getId() +
            ", numOfQuestion=" + getNumOfQuestion() +
            ", questionGroup=" + getQuestionGroup() +
            ", exam=" + getExam() +
            "}";
    }
}
