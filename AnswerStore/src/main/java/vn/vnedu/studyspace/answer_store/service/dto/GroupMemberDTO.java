package vn.vnedu.studyspace.answer_store.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.answer_store.domain.GroupMember} entity.
 */
public class GroupMemberDTO implements Serializable {

    private Long id;

    private String userLogin;

    @Min(value = 0)
    @Max(value = 2)
    private Integer role;

    private Long groupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupMemberDTO)) {
            return false;
        }

        GroupMemberDTO groupMemberDTO = (GroupMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupMemberDTO{" +
            "id=" + getId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", role=" + getRole() +
            ", groupId=" + getGroupId() +
            "}";
    }
}
