package vn.vnedu.studyspace.answer_store.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AnswerSheet.
 */
@Table("answer_sheet")
public class AnswerSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("time")
    private Instant time;

    @NotNull(message = "must not be null")
    @Column("user_login")
    private String userLogin;

    @Transient
    private GroupTimeTable groupTimeTable;

    @Column("group_time_table_id")
    private Long groupTimeTableId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnswerSheet id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getTime() {
        return this.time;
    }

    public AnswerSheet time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public AnswerSheet userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public GroupTimeTable getGroupTimeTable() {
        return this.groupTimeTable;
    }

    public AnswerSheet groupTimeTable(GroupTimeTable groupTimeTable) {
        this.setGroupTimeTable(groupTimeTable);
        this.groupTimeTableId = groupTimeTable != null ? groupTimeTable.getId() : null;
        return this;
    }

    public void setGroupTimeTable(GroupTimeTable groupTimeTable) {
        this.groupTimeTable = groupTimeTable;
        this.groupTimeTableId = groupTimeTable != null ? groupTimeTable.getId() : null;
    }

    public Long getGroupTimeTableId() {
        return this.groupTimeTableId;
    }

    public void setGroupTimeTableId(Long groupTimeTable) {
        this.groupTimeTableId = groupTimeTable;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerSheet)) {
            return false;
        }
        return id != null && id.equals(((AnswerSheet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerSheet{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
