package vn.vnedu.studyspace.exam_store.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionGroup.
 */
@Entity
@Table(name = "question_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestionGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "group_id")
    private String groupId;

    @NotNull
    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @ManyToOne
    private Topic topic;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionGroup id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public QuestionGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public QuestionGroup groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public QuestionGroup userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public QuestionGroup topic(Topic topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionGroup)) {
            return false;
        }
        return id != null && id.equals(((QuestionGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", groupId='" + getGroupId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
