package vn.vnedu.studyspace.answer_store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.answer_store.web.rest.TestUtil;

class AnswerSheetItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerSheetItem.class);
        AnswerSheetItem answerSheetItem1 = new AnswerSheetItem();
        answerSheetItem1.setId(1L);
        AnswerSheetItem answerSheetItem2 = new AnswerSheetItem();
        answerSheetItem2.setId(answerSheetItem1.getId());
        assertThat(answerSheetItem1).isEqualTo(answerSheetItem2);
        answerSheetItem2.setId(2L);
        assertThat(answerSheetItem1).isNotEqualTo(answerSheetItem2);
        answerSheetItem1.setId(null);
        assertThat(answerSheetItem1).isNotEqualTo(answerSheetItem2);
    }
}
