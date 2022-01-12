package vn.vnedu.studyspace.answer_store.service.dto;

import java.util.Objects;

public class UserAnswerDTO extends AnswerDTO {

    private Long examId;

    private Integer count;

    public UserAnswerDTO(){ }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        UserAnswerDTO that = (UserAnswerDTO) o;
        return Objects.equals(examId, that.examId) && Objects.equals(super.getQuestionId(), that.getQuestionId()) && Objects.equals(super.getAnswerId(), that.getAnswerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), examId, count);
    }

    @Override
    public String toString() {
       return "CorrectAnswerDTO{" +
           "questionId=" + super.getQuestionId() +
           ", answerId=" + super.getAnswerId() +
           ", examId=" + examId +
           ", count" + count +
           '}';
    }
}
