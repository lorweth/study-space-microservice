package vn.vnedu.studyspace.answer_store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AnswerSheetItem.
 */
@Table("answer_sheet_item")
public class AnswerSheetItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("question_id")
    private Long questionId;

    @NotNull(message = "must not be null")
    @Column("answer_id")
    private Long answerId;

    @JsonIgnoreProperties(value = { "groupTimeTable" }, allowSetters = true)
    @Transient
    private AnswerSheet answerSheet;

    @Column("answer_sheet_id")
    private Long answerSheetId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnswerSheetItem id(Long id) {
        this.id = id;
        return this;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public AnswerSheetItem questionId(Long questionId) {
        this.questionId = questionId;
        return this;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return this.answerId;
    }

    public AnswerSheetItem answerId(Long answerId) {
        this.answerId = answerId;
        return this;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public AnswerSheet getAnswerSheet() {
        return this.answerSheet;
    }

    public AnswerSheetItem answerSheet(AnswerSheet answerSheet) {
        this.setAnswerSheet(answerSheet);
        this.answerSheetId = answerSheet != null ? answerSheet.getId() : null;
        return this;
    }

    public void setAnswerSheet(AnswerSheet answerSheet) {
        this.answerSheet = answerSheet;
        this.answerSheetId = answerSheet != null ? answerSheet.getId() : null;
    }

    public Long getAnswerSheetId() {
        return this.answerSheetId;
    }

    public void setAnswerSheetId(Long answerSheet) {
        this.answerSheetId = answerSheet;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerSheetItem)) {
            return false;
        }
        return id != null && id.equals(((AnswerSheetItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerSheetItem{" +
            "id=" + getId() +
            ", questionId=" + getQuestionId() +
            ", answerId=" + getAnswerId() +
            "}";
    }
}
