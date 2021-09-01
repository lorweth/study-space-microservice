package vn.vnedu.studyspace.answer_store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.answer_store.domain.TimeTable} entity.
 */
public class TimeTableDTO implements Serializable {

    private Long id;

    @Size(min = 2, max = 255)
    private String title;

    private Instant time;

    @Lob
    private String note;

    @NotNull(message = "must not be null")
    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        if (!(o instanceof TimeTableDTO)) {
            return false;
        }

        TimeTableDTO timeTableDTO = (TimeTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeTableDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", time='" + getTime() + "'" +
            ", note='" + getNote() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
