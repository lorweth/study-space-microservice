package vn.vnedu.studyspace.answer_store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeTableMapperTest {

    private TimeTableMapper timeTableMapper;

    @BeforeEach
    public void setUp() {
        timeTableMapper = new TimeTableMapperImpl();
    }
}
