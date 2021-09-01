package vn.vnedu.studyspace.answer_store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.answer_store.domain.GroupTimeTable} entity.
 */
public class GroupTimeTableDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long examId;

    @NotNull(message = "must not be null")
    private Instant startAt;

    @NotNull(message = "must not be null")
    private Instant endAt;

    @NotNull(message = "must not be null")
    private String groupId;

    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupTimeTableDTO)) {
            return false;
        }

        GroupTimeTableDTO groupTimeTableDTO = (GroupTimeTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupTimeTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupTimeTableDTO{" +
            "id=" + getId() +
            ", examId=" + getExamId() +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", groupId='" + getGroupId() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
