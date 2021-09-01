package vn.vnedu.studyspace.exam_store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vn.vnedu.studyspace.exam_store.web.rest.TestUtil;

class OptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Option.class);
        Option option1 = new Option();
        option1.setId(1L);
        Option option2 = new Option();
        option2.setId(option1.getId());
        assertThat(option1).isEqualTo(option2);
        option2.setId(2L);
        assertThat(option1).isNotEqualTo(option2);
        option1.setId(null);
        assertThat(option1).isNotEqualTo(option2);
    }
}
