package com.ensa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ensa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FraitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Frait.class);
        Frait frait1 = new Frait();
        frait1.setId(1L);
        Frait frait2 = new Frait();
        frait2.setId(frait1.getId());
        assertThat(frait1).isEqualTo(frait2);
        frait2.setId(2L);
        assertThat(frait1).isNotEqualTo(frait2);
        frait1.setId(null);
        assertThat(frait1).isNotEqualTo(frait2);
    }
}
