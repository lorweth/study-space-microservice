package vn.vnedu.studyspace.group_store.service.dto;


import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

public class GroupMemberKafkaDTO implements Serializable {

    private Long id;

    @NotNull
    private String userLogin;

    @NotNull
    private Long groupId;

    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    private Integer role;

    public GroupMemberKafkaDTO() {}

    public GroupMemberKafkaDTO(GroupMemberDTO groupMemberDTO) {
        this.id = groupMemberDTO.getId();
        this.userLogin = groupMemberDTO.getUserLogin();
        this.groupId = groupMemberDTO.getGroup().getId();
        this.role = groupMemberDTO.getRole();
    }

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupMemberKafkaDTO)) {
            return false;
        }

        GroupMemberKafkaDTO groupMemberKafkaDTO = (GroupMemberKafkaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupMemberKafkaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupMemberKafkaDTO{" +
            "id=" + getId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", groupId=" + getGroupId() +
            ", role=" + getRole() +
            "}";
    }
}
