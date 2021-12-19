package vn.vnedu.studyspace.exam_store.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link vn.vnedu.studyspace.exam_store.domain.QuestionGroup} entity.
 */
public class QuestionGroupDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5, max = 255)
    private String name;

    private Long groupId;

    private String userLogin;

    private TopicDTO topic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public TopicDTO getTopic() {
        return topic;
    }

    public void setTopic(TopicDTO topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionGroupDTO)) {
            return false;
        }

        QuestionGroupDTO questionGroupDTO = (QuestionGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", groupId=" + getGroupId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", topic=" + getTopic() +
            "}";
    }
}
