package vn.vnedu.studyspace.answer_store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupTimeTableMapperTest {

    private GroupTimeTableMapper groupTimeTableMapper;

    @BeforeEach
    public void setUp() {
        groupTimeTableMapper = new GroupTimeTableMapperImpl();
    }
}
