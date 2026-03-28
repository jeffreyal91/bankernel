package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PessoaFisicaCriteriaTest {

    @Test
    void newPessoaFisicaCriteriaHasAllFiltersNullTest() {
        var pessoaFisicaCriteria = new PessoaFisicaCriteria();
        assertThat(pessoaFisicaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pessoaFisicaCriteriaFluentMethodsCreatesFiltersTest() {
        var pessoaFisicaCriteria = new PessoaFisicaCriteria();

        setAllFilters(pessoaFisicaCriteria);

        assertThat(pessoaFisicaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pessoaFisicaCriteriaCopyCreatesNullFilterTest() {
        var pessoaFisicaCriteria = new PessoaFisicaCriteria();
        var copy = pessoaFisicaCriteria.copy();

        assertThat(pessoaFisicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pessoaFisicaCriteria)
        );
    }

    @Test
    void pessoaFisicaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pessoaFisicaCriteria = new PessoaFisicaCriteria();
        setAllFilters(pessoaFisicaCriteria);

        var copy = pessoaFisicaCriteria.copy();

        assertThat(pessoaFisicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pessoaFisicaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pessoaFisicaCriteria = new PessoaFisicaCriteria();

        assertThat(pessoaFisicaCriteria).hasToString("PessoaFisicaCriteria{}");
    }

    private static void setAllFilters(PessoaFisicaCriteria pessoaFisicaCriteria) {
        pessoaFisicaCriteria.id();
        pessoaFisicaCriteria.cpf();
        pessoaFisicaCriteria.nomeCompleto();
        pessoaFisicaCriteria.nomeSocial();
        pessoaFisicaCriteria.dataNascimento();
        pessoaFisicaCriteria.genero();
        pessoaFisicaCriteria.nomeMae();
        pessoaFisicaCriteria.telefone();
        pessoaFisicaCriteria.telefoneVerificado();
        pessoaFisicaCriteria.nivelRisco();
        pessoaFisicaCriteria.situacao();
        pessoaFisicaCriteria.bloqueioSaque();
        pessoaFisicaCriteria.usuarioId();
        pessoaFisicaCriteria.moedaPrincipalId();
        pessoaFisicaCriteria.nacionalidadeId();
        pessoaFisicaCriteria.profissaoId();
        pessoaFisicaCriteria.planoId();
        pessoaFisicaCriteria.escritorioId();
        pessoaFisicaCriteria.distinct();
    }

    private static Condition<PessoaFisicaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCpf()) &&
                condition.apply(criteria.getNomeCompleto()) &&
                condition.apply(criteria.getNomeSocial()) &&
                condition.apply(criteria.getDataNascimento()) &&
                condition.apply(criteria.getGenero()) &&
                condition.apply(criteria.getNomeMae()) &&
                condition.apply(criteria.getTelefone()) &&
                condition.apply(criteria.getTelefoneVerificado()) &&
                condition.apply(criteria.getNivelRisco()) &&
                condition.apply(criteria.getSituacao()) &&
                condition.apply(criteria.getBloqueioSaque()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getMoedaPrincipalId()) &&
                condition.apply(criteria.getNacionalidadeId()) &&
                condition.apply(criteria.getProfissaoId()) &&
                condition.apply(criteria.getPlanoId()) &&
                condition.apply(criteria.getEscritorioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PessoaFisicaCriteria> copyFiltersAre(
        PessoaFisicaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCpf(), copy.getCpf()) &&
                condition.apply(criteria.getNomeCompleto(), copy.getNomeCompleto()) &&
                condition.apply(criteria.getNomeSocial(), copy.getNomeSocial()) &&
                condition.apply(criteria.getDataNascimento(), copy.getDataNascimento()) &&
                condition.apply(criteria.getGenero(), copy.getGenero()) &&
                condition.apply(criteria.getNomeMae(), copy.getNomeMae()) &&
                condition.apply(criteria.getTelefone(), copy.getTelefone()) &&
                condition.apply(criteria.getTelefoneVerificado(), copy.getTelefoneVerificado()) &&
                condition.apply(criteria.getNivelRisco(), copy.getNivelRisco()) &&
                condition.apply(criteria.getSituacao(), copy.getSituacao()) &&
                condition.apply(criteria.getBloqueioSaque(), copy.getBloqueioSaque()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getMoedaPrincipalId(), copy.getMoedaPrincipalId()) &&
                condition.apply(criteria.getNacionalidadeId(), copy.getNacionalidadeId()) &&
                condition.apply(criteria.getProfissaoId(), copy.getProfissaoId()) &&
                condition.apply(criteria.getPlanoId(), copy.getPlanoId()) &&
                condition.apply(criteria.getEscritorioId(), copy.getEscritorioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
