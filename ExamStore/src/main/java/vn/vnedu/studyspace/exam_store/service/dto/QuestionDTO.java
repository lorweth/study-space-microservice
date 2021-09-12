package vn.vnedu.studyspace.exam_store.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vn.vnedu.studyspace.exam_store.domain.Option;
import vn.vnedu.studyspace.exam_store.domain.Question;

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

    @JsonIgnoreProperties(value = { "question" }, allowSetters = true)
    private Set<OptionDTO> options = new HashSet<>();

    private QuestionGroupDTO repo;

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

    public Set<OptionDTO> getOptions() {
        return this.options;
    }

    public QuestionDTO options(Set<OptionDTO> options) {
        this.setOptions(options);
        return this;
    }

    public QuestionDTO addOptions(OptionDTO option) {
        this.options.add(option);
        option.setQuestion(this);
        return this;
    }

    public QuestionDTO removeOptions(OptionDTO option) {
        this.options.remove(option);
        option.setQuestion(null);
        return this;
    }

    public void setOptions(Set<OptionDTO> options) {
        if (this.options != null) {
            this.options.forEach(i -> i.setQuestion(null));
        }
        if (options != null) {
            options.forEach(i -> i.setQuestion(this));
        }
        this.options = options;
    }

    public QuestionGroupDTO getRepo() {
        return repo;
    }

    public void setRepo(QuestionGroupDTO repo) {
        this.repo = repo;
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
            ", repo=" + getRepo() +
            "}";
    }
}
