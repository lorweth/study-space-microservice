package vn.vnedu.studyspace.exam_store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionGroupMapperTest {

    private QuestionGroupMapper questionGroupMapper;

    @BeforeEach
    public void setUp() {
        questionGroupMapper = new QuestionGroupMapperImpl();
    }
}
