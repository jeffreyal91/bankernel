package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LancamentoContabilCriteriaTest {

    @Test
    void newLancamentoContabilCriteriaHasAllFiltersNullTest() {
        var lancamentoContabilCriteria = new LancamentoContabilCriteria();
        assertThat(lancamentoContabilCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void lancamentoContabilCriteriaFluentMethodsCreatesFiltersTest() {
        var lancamentoContabilCriteria = new LancamentoContabilCriteria();

        setAllFilters(lancamentoContabilCriteria);

        assertThat(lancamentoContabilCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void lancamentoContabilCriteriaCopyCreatesNullFilterTest() {
        var lancamentoContabilCriteria = new LancamentoContabilCriteria();
        var copy = lancamentoContabilCriteria.copy();

        assertThat(lancamentoContabilCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(lancamentoContabilCriteria)
        );
    }

    @Test
    void lancamentoContabilCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var lancamentoContabilCriteria = new LancamentoContabilCriteria();
        setAllFilters(lancamentoContabilCriteria);

        var copy = lancamentoContabilCriteria.copy();

        assertThat(lancamentoContabilCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(lancamentoContabilCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var lancamentoContabilCriteria = new LancamentoContabilCriteria();

        assertThat(lancamentoContabilCriteria).hasToString("LancamentoContabilCriteria{}");
    }

    private static void setAllFilters(LancamentoContabilCriteria lancamentoContabilCriteria) {
        lancamentoContabilCriteria.id();
        lancamentoContabilCriteria.valor();
        lancamentoContabilCriteria.tipoLancamento();
        lancamentoContabilCriteria.sinalLancamento();
        lancamentoContabilCriteria.ativo();
        lancamentoContabilCriteria.transacaoId();
        lancamentoContabilCriteria.contaContabilId();
        lancamentoContabilCriteria.distinct();
    }

    private static Condition<LancamentoContabilCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValor()) &&
                condition.apply(criteria.getTipoLancamento()) &&
                condition.apply(criteria.getSinalLancamento()) &&
                condition.apply(criteria.getAtivo()) &&
                condition.apply(criteria.getTransacaoId()) &&
                condition.apply(criteria.getContaContabilId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LancamentoContabilCriteria> copyFiltersAre(
        LancamentoContabilCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValor(), copy.getValor()) &&
                condition.apply(criteria.getTipoLancamento(), copy.getTipoLancamento()) &&
                condition.apply(criteria.getSinalLancamento(), copy.getSinalLancamento()) &&
                condition.apply(criteria.getAtivo(), copy.getAtivo()) &&
                condition.apply(criteria.getTransacaoId(), copy.getTransacaoId()) &&
                condition.apply(criteria.getContaContabilId(), copy.getContaContabilId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
