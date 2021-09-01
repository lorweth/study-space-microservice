package vn.vnedu.studyspace.exam_store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OptionMapperTest {

    private OptionMapper optionMapper;

    @BeforeEach
    public void setUp() {
        optionMapper = new OptionMapperImpl();
    }
}
