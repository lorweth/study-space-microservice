package vn.vnedu.studyspace.answer_store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("time")
    private Instant time;

    @NotNull(message = "must not be null")
    @Column("user_login")
    private String userLogin;

    @NotNull(message = "must not be null")
    @Column("exam_id")
    private Long examId;

    @Transient
    @JsonIgnoreProperties(value = { "answerSheet" }, allowSetters = true)
    private Set<AnswerSheetItem> answerSheetItems = new HashSet<>();

    @Transient
    private GroupTimeTable groupTimeTable;

    @Column("group_time_table_id")
    private Long groupTimeTableId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnswerSheet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return this.time;
    }

    public AnswerSheet time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public AnswerSheet userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getExamId() {
        return this.examId;
    }

    public AnswerSheet examId(Long examId) {
        this.setExamId(examId);
        return this;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Set<AnswerSheetItem> getAnswerSheetItems() {
        return this.answerSheetItems;
    }

    public void setAnswerSheetItems(Set<AnswerSheetItem> answerSheetItems) {
        if (this.answerSheetItems != null) {
            this.answerSheetItems.forEach(i -> i.setAnswerSheet(null));
        }
        if (answerSheetItems != null) {
            answerSheetItems.forEach(i -> i.setAnswerSheet(this));
        }
        this.answerSheetItems = answerSheetItems;
    }

    public AnswerSheet answerSheetItems(Set<AnswerSheetItem> answerSheetItems) {
        this.setAnswerSheetItems(answerSheetItems);
        return this;
    }

    public AnswerSheet addAnswerSheetItem(AnswerSheetItem answerSheetItem) {
        this.answerSheetItems.add(answerSheetItem);
        answerSheetItem.setAnswerSheet(this);
        return this;
    }

    public AnswerSheet removeAnswerSheetItem(AnswerSheetItem answerSheetItem) {
        this.answerSheetItems.remove(answerSheetItem);
        answerSheetItem.setAnswerSheet(null);
        return this;
    }

    public GroupTimeTable getGroupTimeTable() {
        return this.groupTimeTable;
    }

    public void setGroupTimeTable(GroupTimeTable groupTimeTable) {
        this.groupTimeTable = groupTimeTable;
        this.groupTimeTableId = groupTimeTable != null ? groupTimeTable.getId() : null;
    }

    public AnswerSheet groupTimeTable(GroupTimeTable groupTimeTable) {
        this.setGroupTimeTable(groupTimeTable);
        return this;
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
            ", examId=" + getExamId() +
            "}";
    }
}
