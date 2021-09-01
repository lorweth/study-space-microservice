package vn.vnedu.studyspace.answer_store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnswerSheetMapperTest {

    private AnswerSheetMapper answerSheetMapper;

    @BeforeEach
    public void setUp() {
        answerSheetMapper = new AnswerSheetMapperImpl();
    }
}
