package vn.vnedu.studyspace.exam_store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.exam_store.web.rest.TestUtil;

class ExamItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamItem.class);
        ExamItem examItem1 = new ExamItem();
        examItem1.setId(1L);
        ExamItem examItem2 = new ExamItem();
        examItem2.setId(examItem1.getId());
        assertThat(examItem1).isEqualTo(examItem2);
        examItem2.setId(2L);
        assertThat(examItem1).isNotEqualTo(examItem2);
        examItem1.setId(null);
        assertThat(examItem1).isNotEqualTo(examItem2);
    }
}
