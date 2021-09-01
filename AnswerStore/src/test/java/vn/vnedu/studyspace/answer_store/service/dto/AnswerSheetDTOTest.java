package vn.vnedu.studyspace.answer_store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.answer_store.web.rest.TestUtil;

class AnswerSheetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerSheetDTO.class);
        AnswerSheetDTO answerSheetDTO1 = new AnswerSheetDTO();
        answerSheetDTO1.setId(1L);
        AnswerSheetDTO answerSheetDTO2 = new AnswerSheetDTO();
        assertThat(answerSheetDTO1).isNotEqualTo(answerSheetDTO2);
        answerSheetDTO2.setId(answerSheetDTO1.getId());
        assertThat(answerSheetDTO1).isEqualTo(answerSheetDTO2);
        answerSheetDTO2.setId(2L);
        assertThat(answerSheetDTO1).isNotEqualTo(answerSheetDTO2);
        answerSheetDTO1.setId(null);
        assertThat(answerSheetDTO1).isNotEqualTo(answerSheetDTO2);
    }
}
