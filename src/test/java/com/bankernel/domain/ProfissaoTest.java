package com.bankernel.domain;

import static com.bankernel.domain.ProfissaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bankernel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfissaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profissao.class);
        Profissao profissao1 = getProfissaoSample1();
        Profissao profissao2 = new Profissao();
        assertThat(profissao1).isNotEqualTo(profissao2);

        profissao2.setId(profissao1.getId());
        assertThat(profissao1).isEqualTo(profissao2);

        profissao2 = getProfissaoSample2();
        assertThat(profissao1).isNotEqualTo(profissao2);
    }
}
