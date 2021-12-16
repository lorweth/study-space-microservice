package vn.vnedu.studyspace.group_store.domain;

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

    @NotNull
    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    @Column(name = "role", nullable = false)
    private Integer role;

    @ManyToOne
    private Group group;

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

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public GroupMember group(Group group) {
        this.setGroup(group);
        return this;
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
            "}";
    }
}
