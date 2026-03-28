package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContaContabilCriteriaTest {

    @Test
    void newContaContabilCriteriaHasAllFiltersNullTest() {
        var contaContabilCriteria = new ContaContabilCriteria();
        assertThat(contaContabilCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contaContabilCriteriaFluentMethodsCreatesFiltersTest() {
        var contaContabilCriteria = new ContaContabilCriteria();

        setAllFilters(contaContabilCriteria);

        assertThat(contaContabilCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contaContabilCriteriaCopyCreatesNullFilterTest() {
        var contaContabilCriteria = new ContaContabilCriteria();
        var copy = contaContabilCriteria.copy();

        assertThat(contaContabilCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contaContabilCriteria)
        );
    }

    @Test
    void contaContabilCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contaContabilCriteria = new ContaContabilCriteria();
        setAllFilters(contaContabilCriteria);

        var copy = contaContabilCriteria.copy();

        assertThat(contaContabilCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contaContabilCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contaContabilCriteria = new ContaContabilCriteria();

        assertThat(contaContabilCriteria).hasToString("ContaContabilCriteria{}");
    }

    private static void setAllFilters(ContaContabilCriteria contaContabilCriteria) {
        contaContabilCriteria.id();
        contaContabilCriteria.codigo();
        contaContabilCriteria.nome();
        contaContabilCriteria.saldo();
        contaContabilCriteria.descricao();
        contaContabilCriteria.tipoContaContabil();
        contaContabilCriteria.categoriaContaContabil();
        contaContabilCriteria.ativa();
        contaContabilCriteria.moedaCarteiraId();
        contaContabilCriteria.distinct();
    }

    private static Condition<ContaContabilCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getSaldo()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getTipoContaContabil()) &&
                condition.apply(criteria.getCategoriaContaContabil()) &&
                condition.apply(criteria.getAtiva()) &&
                condition.apply(criteria.getMoedaCarteiraId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContaContabilCriteria> copyFiltersAre(
        ContaContabilCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getSaldo(), copy.getSaldo()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getTipoContaContabil(), copy.getTipoContaContabil()) &&
                condition.apply(criteria.getCategoriaContaContabil(), copy.getCategoriaContaContabil()) &&
                condition.apply(criteria.getAtiva(), copy.getAtiva()) &&
                condition.apply(criteria.getMoedaCarteiraId(), copy.getMoedaCarteiraId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
