package com.bankernel.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IntegracaoLogCriteriaTest {

    @Test
    void newIntegracaoLogCriteriaHasAllFiltersNullTest() {
        var integracaoLogCriteria = new IntegracaoLogCriteria();
        assertThat(integracaoLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void integracaoLogCriteriaFluentMethodsCreatesFiltersTest() {
        var integracaoLogCriteria = new IntegracaoLogCriteria();

        setAllFilters(integracaoLogCriteria);

        assertThat(integracaoLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void integracaoLogCriteriaCopyCreatesNullFilterTest() {
        var integracaoLogCriteria = new IntegracaoLogCriteria();
        var copy = integracaoLogCriteria.copy();

        assertThat(integracaoLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(integracaoLogCriteria)
        );
    }

    @Test
    void integracaoLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var integracaoLogCriteria = new IntegracaoLogCriteria();
        setAllFilters(integracaoLogCriteria);

        var copy = integracaoLogCriteria.copy();

        assertThat(integracaoLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(integracaoLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var integracaoLogCriteria = new IntegracaoLogCriteria();

        assertThat(integracaoLogCriteria).hasToString("IntegracaoLogCriteria{}");
    }

    private static void setAllFilters(IntegracaoLogCriteria integracaoLogCriteria) {
        integracaoLogCriteria.id();
        integracaoLogCriteria.provider();
        integracaoLogCriteria.tipoIntegracao();
        integracaoLogCriteria.operacao();
        integracaoLogCriteria.statusHttp();
        integracaoLogCriteria.sucesso();
        integracaoLogCriteria.mensagemErro();
        integracaoLogCriteria.duracaoMs();
        integracaoLogCriteria.tipoEntidadeOrigem();
        integracaoLogCriteria.idEntidadeOrigem();
        integracaoLogCriteria.distinct();
    }

    private static Condition<IntegracaoLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProvider()) &&
                condition.apply(criteria.getTipoIntegracao()) &&
                condition.apply(criteria.getOperacao()) &&
                condition.apply(criteria.getStatusHttp()) &&
                condition.apply(criteria.getSucesso()) &&
                condition.apply(criteria.getMensagemErro()) &&
                condition.apply(criteria.getDuracaoMs()) &&
                condition.apply(criteria.getTipoEntidadeOrigem()) &&
                condition.apply(criteria.getIdEntidadeOrigem()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IntegracaoLogCriteria> copyFiltersAre(
        IntegracaoLogCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProvider(), copy.getProvider()) &&
                condition.apply(criteria.getTipoIntegracao(), copy.getTipoIntegracao()) &&
                condition.apply(criteria.getOperacao(), copy.getOperacao()) &&
                condition.apply(criteria.getStatusHttp(), copy.getStatusHttp()) &&
                condition.apply(criteria.getSucesso(), copy.getSucesso()) &&
                condition.apply(criteria.getMensagemErro(), copy.getMensagemErro()) &&
                condition.apply(criteria.getDuracaoMs(), copy.getDuracaoMs()) &&
                condition.apply(criteria.getTipoEntidadeOrigem(), copy.getTipoEntidadeOrigem()) &&
                condition.apply(criteria.getIdEntidadeOrigem(), copy.getIdEntidadeOrigem()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
