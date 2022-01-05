package vn.vnedu.studyspace.exam_store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.exam_store.domain.Exam} entity.
 */
public class ExamDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 155)
    private String name;

    private Instant startAt;

    private Instant endAt;

    @NotNull
    @Min(value = 5)
    @Max(value = 180)
    private Integer duration;

    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    private Integer mix;

    private Long groupId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMix() {
        return mix;
    }

    public void setMix(Integer mix) {
        this.mix = mix;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamDTO)) {
            return false;
        }

        ExamDTO examDTO = (ExamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", duration=" + getDuration() +
            ", mix=" + getMix() +
            ", groupId=" + getGroupId() +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
