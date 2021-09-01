package vn.vnedu.studyspace.answer_store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.answer_store.web.rest.TestUtil;

class AnswerSheetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerSheet.class);
        AnswerSheet answerSheet1 = new AnswerSheet();
        answerSheet1.setId(1L);
        AnswerSheet answerSheet2 = new AnswerSheet();
        answerSheet2.setId(answerSheet1.getId());
        assertThat(answerSheet1).isEqualTo(answerSheet2);
        answerSheet2.setId(2L);
        assertThat(answerSheet1).isNotEqualTo(answerSheet2);
        answerSheet1.setId(null);
        assertThat(answerSheet1).isNotEqualTo(answerSheet2);
    }
}
