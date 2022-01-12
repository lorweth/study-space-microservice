package vn.vnedu.studyspace.answer_store.service.dto;

import java.time.Instant;
import java.util.Objects;

public class SummaryResultDTO {
    private Long sheetId;
    private Integer wrongAnswerCount;
    private Instant time;

    public Long getSheetId() {
        return sheetId;
    }

    public void setSheetId(Long sheetId) {
        this.sheetId = sheetId;
    }

    public Integer getWrongAnswerCount() {
        return wrongAnswerCount;
    }

    public void setWrongAnswerCount(Integer wrongAnswerCount) {
        this.wrongAnswerCount = wrongAnswerCount;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryResultDTO that = (SummaryResultDTO) o;
        return Objects.equals(sheetId, that.sheetId) && Objects.equals(wrongAnswerCount, that.wrongAnswerCount) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetId, wrongAnswerCount, time);
    }

    @Override
    public String toString() {
        return "SummaryResultDTO{" +
            "sheetId=" + sheetId +
            ", wrongAnswerCount=" + wrongAnswerCount +
            ", time=" + time +
            '}';
    }
}
