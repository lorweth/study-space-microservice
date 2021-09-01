package vn.vnedu.studyspace.answer_store.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TimeTable.
 */
@Table("time_table")
public class TimeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Size(min = 2, max = 255)
    @Column("title")
    private String title;

    @Column("time")
    private Instant time;

    @Column("note")
    private String note;

    @NotNull(message = "must not be null")
    @Column("user_login")
    private String userLogin;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeTable id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public TimeTable title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getTime() {
        return this.time;
    }

    public TimeTable time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getNote() {
        return this.note;
    }

    public TimeTable note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public TimeTable userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeTable)) {
            return false;
        }
        return id != null && id.equals(((TimeTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeTable{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", time='" + getTime() + "'" +
            ", note='" + getNote() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
