package vn.vnedu.studyspace.answer_store.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CorrectAnswerDTO {

    @NotNull
    private Long questionId;

    private String questionContent;

    @NotNull
    private Long answerId;

    private String answerContent;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorrectAnswerDTO that = (CorrectAnswerDTO) o;
        return Objects.equals(questionId, that.questionId) && Objects.equals(answerId, that.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, answerId);
    }

    @Override
    public String toString() {
        return "CorrectAnswerDTO{" +
            "questionId=" + questionId +
            ", answerId=" + answerId +
            '}';
    }
}
