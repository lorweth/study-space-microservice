package vn.vnedu.studyspace.answer_store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.answer_store.web.rest.TestUtil;

class GroupTimeTableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupTimeTableDTO.class);
        GroupTimeTableDTO groupTimeTableDTO1 = new GroupTimeTableDTO();
        groupTimeTableDTO1.setId(1L);
        GroupTimeTableDTO groupTimeTableDTO2 = new GroupTimeTableDTO();
        assertThat(groupTimeTableDTO1).isNotEqualTo(groupTimeTableDTO2);
        groupTimeTableDTO2.setId(groupTimeTableDTO1.getId());
        assertThat(groupTimeTableDTO1).isEqualTo(groupTimeTableDTO2);
        groupTimeTableDTO2.setId(2L);
        assertThat(groupTimeTableDTO1).isNotEqualTo(groupTimeTableDTO2);
        groupTimeTableDTO1.setId(null);
        assertThat(groupTimeTableDTO1).isNotEqualTo(groupTimeTableDTO2);
    }
}
