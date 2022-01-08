package vn.vnedu.studyspace.answer_store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.answer_store.domain.AnswerSheet} entity.
 */
public class AnswerSheetDTO implements Serializable {

    private Long id;

    private Instant time;

    private String userLogin;

    @NotNull(message = "must not be null")
    private Long examId;

    private GroupTimeTableDTO groupTimeTable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public GroupTimeTableDTO getGroupTimeTable() {
        return groupTimeTable;
    }

    public void setGroupTimeTable(GroupTimeTableDTO groupTimeTable) {
        this.groupTimeTable = groupTimeTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerSheetDTO)) {
            return false;
        }

        AnswerSheetDTO answerSheetDTO = (AnswerSheetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, answerSheetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerSheetDTO{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", examId=" + getExamId() +
            ", groupTimeTable=" + getGroupTimeTable() +
            "}";
    }
}
