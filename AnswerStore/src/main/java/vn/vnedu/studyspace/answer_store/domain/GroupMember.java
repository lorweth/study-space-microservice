package vn.vnedu.studyspace.answer_store.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A GroupMember.
 */
@Table("group_member")
public class GroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("user_login")
    private String userLogin;

    @NotNull(message = "must not be null")
    @Column("group_id")
    private Long groupId;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Max(value = 2)
    @Column("role")
    private Integer role;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupMember id(Long id) {
        this.id = id;
        return this;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public GroupMember userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public GroupMember groupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getRole() {
        return this.role;
    }

    public GroupMember role(Integer role) {
        this.role = role;
        return this;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupMember)) {
            return false;
        }
        return id != null && id.equals(((GroupMember) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupMember{" +
            "id=" + getId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", groupId=" + getGroupId() +
            ", role=" + getRole() +
            "}";
    }
}
