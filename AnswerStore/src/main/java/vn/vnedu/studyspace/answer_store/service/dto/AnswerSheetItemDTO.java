package vn.vnedu.studyspace.answer_store.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem} entity.
 */
public class AnswerSheetItemDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long questionId;

    @NotNull(message = "must not be null")
    private Long answerId;

    private AnswerSheetDTO answerSheet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public AnswerSheetDTO getAnswerSheet() {
        return answerSheet;
    }

    public void setAnswerSheet(AnswerSheetDTO answerSheet) {
        this.answerSheet = answerSheet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerSheetItemDTO)) {
            return false;
        }

        AnswerSheetItemDTO answerSheetItemDTO = (AnswerSheetItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, answerSheetItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerSheetItemDTO{" +
            "id=" + getId() +
            ", questionId=" + getQuestionId() +
            ", answerId=" + getAnswerId() +
            ", answerSheet=" + getAnswerSheet() +
            "}";
    }
}
