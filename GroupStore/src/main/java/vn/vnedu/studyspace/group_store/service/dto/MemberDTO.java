package vn.vnedu.studyspace.group_store.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class MemberDTO implements Serializable {
    private Long id;

    @NotNull
    private String userLogin;

    @NotNull
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

    public void setGroup(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDTO)) {
            return false;
        }

        MemberDTO memberDTO = (MemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberDTO.id);
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
