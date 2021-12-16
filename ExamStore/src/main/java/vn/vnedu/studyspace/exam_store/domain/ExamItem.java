package vn.vnedu.studyspace.exam_store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExamItem.
 */
@Entity
@Table(name = "exam_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExamItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num_of_question", nullable = false)
    private Integer numOfQuestion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "topic" }, allowSetters = true)
    private QuestionGroup questionGroup;

    @ManyToOne
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Exam exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumOfQuestion() {
        return this.numOfQuestion;
    }

    public ExamItem numOfQuestion(Integer numOfQuestion) {
        this.setNumOfQuestion(numOfQuestion);
        return this;
    }

    public void setNumOfQuestion(Integer numOfQuestion) {
        this.numOfQuestion = numOfQuestion;
    }

    public QuestionGroup getQuestionGroup() {
        return this.questionGroup;
    }

    public void setQuestionGroup(QuestionGroup questionGroup) {
        this.questionGroup = questionGroup;
    }

    public ExamItem questionGroup(QuestionGroup questionGroup) {
        this.setQuestionGroup(questionGroup);
        return this;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public ExamItem exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamItem)) {
            return false;
        }
        return id != null && id.equals(((ExamItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamItem{" +
            "id=" + getId() +
            ", numOfQuestion=" + getNumOfQuestion() +
            "}";
    }
}
