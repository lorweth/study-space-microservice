package vn.vnedu.studyspace.exam_store.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.exam_store.domain.Question} entity.
 */
public class QuestionDTO implements Serializable {

    private Long id;

    @Lob
    private String content;

    @Lob
    private String note;

    @NotNull
    private QuestionGroupDTO questionGroup;

    private Set<OptionDTO> options;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public QuestionGroupDTO getQuestionGroup() {
        return questionGroup;
    }

    public void setQuestionGroup(QuestionGroupDTO questionGroup) {
        this.questionGroup = questionGroup;
    }

    public Set<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(Set<OptionDTO> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionDTO)) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", note='" + getNote() + "'" +
            ", questionGroup=" + getQuestionGroup() +
            "}";
    }
}
