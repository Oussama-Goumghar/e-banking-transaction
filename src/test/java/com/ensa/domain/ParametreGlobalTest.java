package com.ensa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ensa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametreGlobalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParametreGlobal.class);
        ParametreGlobal parametreGlobal1 = new ParametreGlobal();
        parametreGlobal1.setId(1L);
        ParametreGlobal parametreGlobal2 = new ParametreGlobal();
        parametreGlobal2.setId(parametreGlobal1.getId());
        assertThat(parametreGlobal1).isEqualTo(parametreGlobal2);
        parametreGlobal2.setId(2L);
        assertThat(parametreGlobal1).isNotEqualTo(parametreGlobal2);
        parametreGlobal1.setId(null);
        assertThat(parametreGlobal1).isNotEqualTo(parametreGlobal2);
    }
}
