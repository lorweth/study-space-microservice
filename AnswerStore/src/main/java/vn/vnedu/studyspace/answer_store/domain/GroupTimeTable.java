package vn.vnedu.studyspace.answer_store.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A GroupTimeTable.
 */
@Table("group_time_table")
public class GroupTimeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("exam_id")
    private Long examId;

    @NotNull(message = "must not be null")
    @Column("start_at")
    private Instant startAt;

    @NotNull(message = "must not be null")
    @Column("end_at")
    private Instant endAt;

    @NotNull(message = "must not be null")
    @Column("group_id")
    private String groupId;

    @Column("note")
    private String note;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupTimeTable id(Long id) {
        this.id = id;
        return this;
    }

    public Long getExamId() {
        return this.examId;
    }

    public GroupTimeTable examId(Long examId) {
        this.examId = examId;
        return this;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Instant getStartAt() {
        return this.startAt;
    }

    public GroupTimeTable startAt(Instant startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return this.endAt;
    }

    public GroupTimeTable endAt(Instant endAt) {
        this.endAt = endAt;
        return this;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public GroupTimeTable groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNote() {
        return this.note;
    }

    public GroupTimeTable note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupTimeTable)) {
            return false;
        }
        return id != null && id.equals(((GroupTimeTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupTimeTable{" +
            "id=" + getId() +
            ", examId=" + getExamId() +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", groupId='" + getGroupId() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
