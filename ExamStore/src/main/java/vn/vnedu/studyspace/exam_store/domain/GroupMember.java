package vn.vnedu.studyspace.exam_store.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GroupMember.
 */
@Entity
@Table(name = "group_member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_login")
    private String userLogin;

    @Min(value = 0)
    @Max(value = 2)
    @Column(name = "role")
    private Integer role;

    @Column(name = "group_id")
    private Long groupId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GroupMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public GroupMember userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Integer getRole() {
        return this.role;
    }

    public GroupMember role(Integer role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public GroupMember groupId(Long groupId) {
        this.setGroupId(groupId);
        return this;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
            ", role=" + getRole() +
            ", groupId=" + getGroupId() +
            "}";
    }
}
