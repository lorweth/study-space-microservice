package vn.vnedu.studyspace.exam_store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.exam_store.web.rest.TestUtil;

class ExamItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamItemDTO.class);
        ExamItemDTO examItemDTO1 = new ExamItemDTO();
        examItemDTO1.setId(1L);
        ExamItemDTO examItemDTO2 = new ExamItemDTO();
        assertThat(examItemDTO1).isNotEqualTo(examItemDTO2);
        examItemDTO2.setId(examItemDTO1.getId());
        assertThat(examItemDTO1).isEqualTo(examItemDTO2);
        examItemDTO2.setId(2L);
        assertThat(examItemDTO1).isNotEqualTo(examItemDTO2);
        examItemDTO1.setId(null);
        assertThat(examItemDTO1).isNotEqualTo(examItemDTO2);
    }
}
