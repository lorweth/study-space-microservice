package vn.vnedu.studyspace.exam_store.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.exam_store.domain.Option} entity.
 */
public class OptionDTO implements Serializable {

    private Long id;

    @Lob
    private String content;

    @NotNull
    private Boolean isCorrect;

    @JsonIgnoreProperties(value = { "options", "repo" }, allowSetters = true)
    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionDTO)) {
            return false;
        }

        OptionDTO optionDTO = (OptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, optionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", isCorrect='" + getIsCorrect() + "'" +
            ", question=" + getQuestion() +
            "}";
    }
}
