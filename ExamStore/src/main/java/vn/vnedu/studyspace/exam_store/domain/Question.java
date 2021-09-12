package vn.vnedu.studyspace.exam_store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Lob
    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "question" }, allowSetters = true)
    private Set<Option> options = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "topic" }, allowSetters = true)
    private QuestionGroup repo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Question content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return this.note;
    }

    public Question note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public Question options(Set<Option> options) {
        this.setOptions(options);
        return this;
    }

    public Question addOptions(Option option) {
        this.options.add(option);
        option.setQuestion(this);
        return this;
    }

    public Question removeOptions(Option option) {
        this.options.remove(option);
        option.setQuestion(null);
        return this;
    }

    public void setOptions(Set<Option> options) {
        if (this.options != null) {
            this.options.forEach(i -> i.setQuestion(null));
        }
        if (options != null) {
            options.forEach(i -> i.setQuestion(this));
        }
        this.options = options;
    }

    public QuestionGroup getRepo() {
        return this.repo;
    }

    public Question repo(QuestionGroup questionGroup) {
        this.setRepo(questionGroup);
        return this;
    }

    public void setRepo(QuestionGroup questionGroup) {
        this.repo = questionGroup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
