package vn.vnedu.studyspace.answer_store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.answer_store.web.rest.TestUtil;

class GroupTimeTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupTimeTable.class);
        GroupTimeTable groupTimeTable1 = new GroupTimeTable();
        groupTimeTable1.setId(1L);
        GroupTimeTable groupTimeTable2 = new GroupTimeTable();
        groupTimeTable2.setId(groupTimeTable1.getId());
        assertThat(groupTimeTable1).isEqualTo(groupTimeTable2);
        groupTimeTable2.setId(2L);
        assertThat(groupTimeTable1).isNotEqualTo(groupTimeTable2);
        groupTimeTable1.setId(null);
        assertThat(groupTimeTable1).isNotEqualTo(groupTimeTable2);
    }
}
