package vn.vnedu.studyspace.exam_store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.exam_store.web.rest.TestUtil;

class QuestionGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionGroupDTO.class);
        QuestionGroupDTO questionGroupDTO1 = new QuestionGroupDTO();
        questionGroupDTO1.setId(1L);
        QuestionGroupDTO questionGroupDTO2 = new QuestionGroupDTO();
        assertThat(questionGroupDTO1).isNotEqualTo(questionGroupDTO2);
        questionGroupDTO2.setId(questionGroupDTO1.getId());
        assertThat(questionGroupDTO1).isEqualTo(questionGroupDTO2);
        questionGroupDTO2.setId(2L);
        assertThat(questionGroupDTO1).isNotEqualTo(questionGroupDTO2);
        questionGroupDTO1.setId(null);
        assertThat(questionGroupDTO1).isNotEqualTo(questionGroupDTO2);
    }
}
