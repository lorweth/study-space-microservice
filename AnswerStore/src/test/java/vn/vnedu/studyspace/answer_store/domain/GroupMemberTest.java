package vn.vnedu.studyspace.answer_store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.answer_store.web.rest.TestUtil;

class GroupMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupMember.class);
        GroupMember groupMember1 = new GroupMember();
        groupMember1.setId(1L);
        GroupMember groupMember2 = new GroupMember();
        groupMember2.setId(groupMember1.getId());
        assertThat(groupMember1).isEqualTo(groupMember2);
        groupMember2.setId(2L);
        assertThat(groupMember1).isNotEqualTo(groupMember2);
        groupMember1.setId(null);
        assertThat(groupMember1).isNotEqualTo(groupMember2);
    }
}
