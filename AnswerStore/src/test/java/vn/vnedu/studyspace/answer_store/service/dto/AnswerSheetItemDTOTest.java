package vn.vnedu.studyspace.answer_store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.answer_store.web.rest.TestUtil;

class AnswerSheetItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerSheetItemDTO.class);
        AnswerSheetItemDTO answerSheetItemDTO1 = new AnswerSheetItemDTO();
        answerSheetItemDTO1.setId(1L);
        AnswerSheetItemDTO answerSheetItemDTO2 = new AnswerSheetItemDTO();
        assertThat(answerSheetItemDTO1).isNotEqualTo(answerSheetItemDTO2);
        answerSheetItemDTO2.setId(answerSheetItemDTO1.getId());
        assertThat(answerSheetItemDTO1).isEqualTo(answerSheetItemDTO2);
        answerSheetItemDTO2.setId(2L);
        assertThat(answerSheetItemDTO1).isNotEqualTo(answerSheetItemDTO2);
        answerSheetItemDTO1.setId(null);
        assertThat(answerSheetItemDTO1).isNotEqualTo(answerSheetItemDTO2);
    }
}
