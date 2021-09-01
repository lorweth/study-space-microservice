package vn.vnedu.studyspace.exam_store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupMemberMapperTest {

    private GroupMemberMapper groupMemberMapper;

    @BeforeEach
    public void setUp() {
        groupMemberMapper = new GroupMemberMapperImpl();
    }
}
