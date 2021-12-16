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
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 155)
    @Column(name = "name", length = 155, nullable = false)
    private String name;

    @NotNull
    @Min(value = 5)
    @Max(value = 180)
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    @Column(name = "mix", nullable = false)
    private Integer mix;

    @NotNull
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @OneToMany(mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questionGroup", "exam" }, allowSetters = true)
    private Set<ExamItem> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Exam name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Exam duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMix() {
        return this.mix;
    }

    public Exam mix(Integer mix) {
        this.setMix(mix);
        return this;
    }

    public void setMix(Integer mix) {
        this.mix = mix;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public Exam groupId(Long groupId) {
        this.setGroupId(groupId);
        return this;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Set<ExamItem> getItems() {
        return this.items;
    }

    public void setItems(Set<ExamItem> examItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setExam(null));
        }
        if (examItems != null) {
            examItems.forEach(i -> i.setExam(this));
        }
        this.items = examItems;
    }

    public Exam items(Set<ExamItem> examItems) {
        this.setItems(examItems);
        return this;
    }

    public Exam addItems(ExamItem examItem) {
        this.items.add(examItem);
        examItem.setExam(this);
        return this;
    }

    public Exam removeItems(ExamItem examItem) {
        this.items.remove(examItem);
        examItem.setExam(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exam)) {
            return false;
        }
        return id != null && id.equals(((Exam) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", duration=" + getDuration() +
            ", mix=" + getMix() +
            ", groupId=" + getGroupId() +
            "}";
    }
}
