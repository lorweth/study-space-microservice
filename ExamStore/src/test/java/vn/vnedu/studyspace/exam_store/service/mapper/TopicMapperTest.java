package vn.vnedu.studyspace.exam_store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TopicMapperTest {

    private TopicMapper topicMapper;

    @BeforeEach
    public void setUp() {
        topicMapper = new TopicMapperImpl();
    }
}
